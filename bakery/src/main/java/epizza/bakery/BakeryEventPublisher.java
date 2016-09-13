package epizza.bakery;

import epizza.shared.event.EventPublisher;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BakeryEventPublisher {

    private final EventPublisher eventPublisher;

    public void sendBakingOrderReceivedEvent(BakeryOrderReceivedEvent event) {
        Map<String, Object> payload = ImmutableMap.of(
                        "orderLink", event.getOrderLink(),
                        "estimatedTimeOfCompletion", event.getEstimatedTimeOfCompletion()
        );
        eventPublisher.publish("BakingOrderReceived", payload);
    }

    public void sendBakingFinishedEvent(URI orderLink) {
        Map<String, Object> payload = ImmutableMap.of("orderLink", orderLink);
        eventPublisher.publish("BakingFinished", payload);
    }
}
