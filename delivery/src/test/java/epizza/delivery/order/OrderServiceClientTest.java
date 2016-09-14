package epizza.delivery.order;

import epizza.delivery.DeliveryApplication;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

import org.assertj.core.api.BDDAssertions;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedResources;
import org.springframework.test.context.junit4.SpringRunner;

import com.epages.wiremock.starter.WireMockTest;
import com.github.tomakehurst.wiremock.WireMockServer;

@Ignore
@WireMockTest(stubPath="wiremock/order")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=DeliveryApplication.class, properties = {"orders.baseUri=http://localhost:${wiremock.port}/"})
public class OrderServiceClientTest {

    @Autowired
    private OrderServiceClient client;

    @Autowired
    private WireMockServer wiremockServer;

    @Test
    @SneakyThrows
    public void should_list_orders() {

        // WHEN
        PagedResources<Order> orders = client.getOrders(new PageRequest(0, 10));

        // THEN
        BDDAssertions.then(orders.getContent())
        .hasSize(1);
    }

    @Test
    public void should_assign_to_order() {
        // WHEN
        client.assignMyselfToOrder(4, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));
    }

    @After
    public void dump_communication_failures() {
        wiremockServer.findAllUnmatchedRequests().stream().forEach((n) -> System.out.println("logged unmatched request:  " + n));
    }
}
