package epizza.bakery;

import epizza.shared.event.EventPublisher;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
public class BakeryEventPublisher {

    private EventPublisher eventPublisher;

    private static final String BAKING_ORDER_RECEIVED_EVENT_TYPE = "BakingOrderReceived";
    private static final String BAKING_FINISHED_EVENT_TYPE = "BakingFinished";

    @Autowired
    public BakeryEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void sendBakingOrderReceivedEvent(BakeryOrderReceivedEvent event) {
        Map<String, Object> payloadMap = ImmutableMap.of(
                "orderLink", event.getOrderLink(),
                "estimatedTimeOfCompletion", event.getEstimatedTimeOfCompletion());
        eventPublisher.publish(BAKING_ORDER_RECEIVED_EVENT_TYPE, payloadMap);
    }

    public void sendBakingFinishedEvent(URI orderLink) {
        Map<String, Object> payloadMap = ImmutableMap.of(
                "orderLink", orderLink);
        eventPublisher.publish(BAKING_FINISHED_EVENT_TYPE, payloadMap);
    }
}
