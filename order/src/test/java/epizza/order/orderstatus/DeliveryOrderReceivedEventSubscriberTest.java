package epizza.order.orderstatus;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import epizza.order.Order;
import epizza.order.OrderApplicationTest;
import epizza.order.OrderService;
import epizza.order.OrderStatus;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@OrderApplicationTest(activeProfiles = {"test", "DeliveryOrderReceivedEventSubscriberTest"})
public class DeliveryOrderReceivedEventSubscriberTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private DeliveryOrderReceivedEventSubscriber deliveryOrderReceivedEventSubscriber;

    private String event = "{\n" +
            "  \"type\" : \"DeliveryOrderReceived\",\n" +
            "  \"timestamp\" : \"2015-09-01T21:43:48.391\",\n" +
            "  \"payload\" : {\n" +
            "      \"orderLink\" : \"http://192.168.99.100:8081/orders/1\",\n" +
            "      \"estimatedTimeOfDelivery\" : \"2015-09-12T07:43:48.391\"\n" +
            "  }\n" +
            "}";

    private Order order;

    @Test
    public void should_set_status_and_date_on_order() {
        givenOrder();

        whenEventConsumed();

        then(order.getStatus()).isEqualTo(OrderStatus.BAKING);
        then(order.getEstimatedTimeOfDelivery()).isNotNull();
        then(order.getEstimatedTimeOfDelivery().getDayOfMonth()).isEqualTo(12);
        then(order.getEstimatedTimeOfDelivery().getHour()).isEqualTo(7);
    }

    private void whenEventConsumed() {
        deliveryOrderReceivedEventSubscriber.consume(event);
    }

    private void givenOrder() {
        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.NEW);

        when(orderService.getOrder(1L)).thenReturn(Optional.of(order));
    }
}
