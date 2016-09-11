package epizza.order.orderstatus;

import epizza.order.Order;
import epizza.order.OrderService;
import epizza.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeliveryOrderReceivedEventSubscriber extends OrderStatusEventSubscriber {

    @Autowired
    public DeliveryOrderReceivedEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, "DeliveryOrderReceived", OrderStatus.BAKING);
    }

    @Override
    protected void enhanceOrder(Order order, Map<String, Object> payload) {
        String payloadString = payload.get("estimatedTimeOfDelivery").toString();
        order.setEstimatedTimeOfDelivery(LocalDateTime.parse(payloadString));
    }
}
