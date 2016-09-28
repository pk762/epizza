package epizza.delivery.order;

import com.epages.wiremock.starter.WireMockTest;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import epizza.delivery.DeliveryApplicationTest;
import lombok.SneakyThrows;

import static org.assertj.core.api.BDDAssertions.then;

@WireMockTest(stubPath = "wiremock/order")
@RunWith(SpringRunner.class)
@DeliveryApplicationTest(properties = {
        "orders.baseUri=http://localhost:${wiremock.port}/",
        "hystrix.command.getOrders.execution.timeout.enabled=true",
        "hystrix.command.getOrders.execution.isolation.thread.timeoutInMilliseconds=2000"
})
public class OrderServiceClientImplTest {

    @Autowired
    private OrderServiceClient client;

    @Autowired
    private WireMockServer wiremockServer;

    @Test
    @SneakyThrows
    public void should_list_orders() {

        // WHEN
        PagedResources<Order> orders = client.getOrders();

        // THEN
        then(orders.getContent()).hasSize(1);
    }

    @Test
    public void should_select_one_order() {
        // WHEN
        client.selectOrder(1, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));
    }

    @Test
    public void should_use_fallback_on_timeout() {
        wiremockServer.setGlobalFixedDelay(2001);
        PagedResources<Order> orders = client.getOrders();
        then(orders.getContent()).hasSize(0);
    }

    @After
    public void dump_communication_failures() {
        wiremockServer.setGlobalFixedDelay(0);
        wiremockServer.findAllUnmatchedRequests().forEach((n) -> System.out.println("Logged unmatched request:  " + n));
    }

}
