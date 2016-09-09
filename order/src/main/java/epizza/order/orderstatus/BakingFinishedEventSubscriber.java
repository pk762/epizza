package epizza.order.orderstatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import epizza.order.OrderService;
import epizza.order.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BakingFinishedEventSubscriber extends epizza.order.orderstatus.OrderStatusEventSubscriber {

    private static final String BAKING_FINISHED_EVENT_TYPE = "BakingFinished";

    @Autowired
    public BakingFinishedEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, BAKING_FINISHED_EVENT_TYPE, OrderStatus.READY_FOR_DELIVERY);
    }
}
