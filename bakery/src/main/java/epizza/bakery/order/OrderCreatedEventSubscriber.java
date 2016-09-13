package epizza.bakery.order;

import epizza.bakery.BakeryService;
import epizza.shared.event.AbstractEventSubscriber;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderCreatedEventSubscriber extends AbstractEventSubscriber {

    private static final String ORDER_CREATED_EVENT_TYPE = "OrderCreated";
    private BakeryService bakeryService;

    @Autowired
    public OrderCreatedEventSubscriber(ObjectMapper objectMapper, BakeryService bakeryService) {
        super(objectMapper, ORDER_CREATED_EVENT_TYPE);
        this.bakeryService = bakeryService;
    }

    @Override
    protected void handleOwnType(Map<String, Object> event) {
        final Map<String, Object> payload = getPayload(event);
        String orderUriString = (String) payload.get("orderLink");
        URI orderUri = URI.create(orderUriString);
        bakeryService.acknowledgeOrder(orderUri);
        bakeryService.bakeOrder(orderUri);
    }
}
