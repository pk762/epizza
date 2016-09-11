package epizza.order.orderstatus;

import epizza.order.OrderService;
import epizza.order.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeliveredEventSubscriber extends OrderStatusEventSubscriber {

    @Autowired
    public DeliveredEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, "Delivered", OrderStatus.DELIVERED);
    }
}
