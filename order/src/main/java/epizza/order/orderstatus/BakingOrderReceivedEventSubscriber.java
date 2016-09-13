package epizza.order.orderstatus;

import epizza.order.Order;
import epizza.order.OrderService;
import epizza.order.OrderStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BakingOrderReceivedEventSubscriber extends OrderStatusEventSubscriber {

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
