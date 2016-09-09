package epizza.order;

import epizza.shared.event.EventPublisher;
import lombok.AllArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderEventPublisher {

    private static final String ORDER_CREATED_EVENT_TYPE = "OrderCreated";

    private final EventPublisher eventPublisher;
    private final EntityLinks entityLinks;


    public void sendOrderCreatedEvent(final Order order) {
        Map<String, Object> payloadMap = ImmutableMap.of(
                "orderLink", entityLinks.linkForSingleResource(Order.class, order.getId()).toUri().toString());
        eventPublisher.publish(ORDER_CREATED_EVENT_TYPE, payloadMap);
    }
}
