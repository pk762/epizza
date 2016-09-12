package epizza.delivery;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DeliveryApplicationTest
public class BakingOrderReceivedEventSubscriberTest {

    @Autowired
    private BakingOrderReceivedEventSubscriber bakingOrderReceivedEventSubscriber;

    @MockBean
    private DeliveryEventPublisher deliveryEventPublisher;

    @Captor
    private ArgumentCaptor<DeliveryOrderReceivedEvent> deliveryEventCaptor;

    private String event = "{\n" +
            "  \"type\" : \"BakingOrderReceived\",\n" +
            "  \"timestamp\" : \"2015-09-01T21:43:48.391\",\n" +
            "  \"payload\" : {\n" +
            "      \"orderLink\" : \"http://192.168.99.100:8081/orders/1\",\n" +
            "      \"estimatedTimeOfCompletion\" : \"2015-09-12T07:43:48.391\"\n" +
            "  }\n" +
            "}";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_schedule_delivery_and_publish_event() {
        bakingOrderReceivedEventSubscriber.consume(event);

        verify(deliveryEventPublisher).sendDeliveryOrderReceivedEvent(deliveryEventCaptor.capture());
        then(deliveryEventCaptor.getValue().getOrderLink()).isNotNull();
        then(deliveryEventCaptor.getValue().getOrderLink().toString()).endsWith("1");
        then(deliveryEventCaptor.getValue().getEstimatedTimeOfDelivery()).isNotNull();
    }

}
