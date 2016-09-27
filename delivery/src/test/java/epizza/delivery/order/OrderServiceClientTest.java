package epizza.delivery.order;

import static org.assertj.core.api.BDDAssertions.then;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import com.epages.wiremock.starter.WireMockTest;
import com.github.tomakehurst.wiremock.WireMockServer;

import epizza.delivery.DeliveryApplicationTest;
import lombok.SneakyThrows;

@WireMockTest(stubPath = "wiremock/order")
@RunWith(SpringRunner.class)
@DeliveryApplicationTest(properties = {"orders.baseUri=http://localhost:${wiremock.port}/"})
public class OrderServiceClientTest {

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
    public void should_assign_to_order() {
        // WHEN
        client.assignMyselfToOrder(1, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));
    }

    @After
    public void dump_communication_failures() {
        wiremockServer.findAllUnmatchedRequests().forEach((n) -> System.out.println("logged unmatched request:  " + n));
    }
}
