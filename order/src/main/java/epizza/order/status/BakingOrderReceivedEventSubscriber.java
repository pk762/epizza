package epizza.order.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import epizza.order.checkout.Order;
import epizza.order.checkout.OrderService;

@Component
public class BakingOrderReceivedEventSubscriber extends AbstractOrderStatusEventSubscriber {

    @Autowired
    public BakingOrderReceivedEventSubscriber(OrderService orderService, ObjectMapper objectMapper) {
        super(orderService, objectMapper, "BakingOrderReceived", OrderStatus.BAKING);
    }

    @Override
    protected void enhanceOrder(Order order, Map<String, Object> payload) {
        LocalDateTime estimatedTimeOfCompletion = LocalDateTime
                .parse(payload.get("estimatedTimeOfCompletion").toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        order.setEstimatedTimeOfBakingCompletion(estimatedTimeOfCompletion);
    }
}
