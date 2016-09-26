package epizza.order.orderstatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import epizza.order.OrderService;
import epizza.order.OrderStatus;

@Component
public class BakingFinishedEventSubscriber extends OrderStatusEventSubscriber {

    @Autowired
    public BakingFinishedEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, "BakingFinished", OrderStatus.DELIVERING);
    }
}
