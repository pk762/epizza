package epizza.order.orderstatus;

import epizza.order.OrderService;
import epizza.order.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BakingFinishedEventSubscriber extends epizza.order.orderstatus.OrderStatusEventSubscriber {

    @Autowired
    public BakingFinishedEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, "BakingFinished", OrderStatus.READY_FOR_DELIVERY);
    }
}
