package epizza.delivery.order;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.epages.wiremock.starter.WireMockTest;

import epizza.delivery.DeliveryApplication;

@WireMockTest(stubPath="wiremock/order")
@RunWith(SpringRunner.class)
@SpringBootTest(classes=DeliveryApplication.class, properties = {"orders.baseUri=http://localhost:${wiremock.port}/"})
public class OrderServiceAssigmentClientTest {

    @Autowired
    private OrderServiceAssigmentClient client;

    @Test
    public void test() {
        // WHEN
        client.assignMyselfToOrder(4, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));
    }

}
