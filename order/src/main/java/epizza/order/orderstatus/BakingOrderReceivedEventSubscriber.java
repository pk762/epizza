package epizza.order.orderstatus;

import epizza.order.OrderService;
import epizza.order.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BakingOrderReceivedEventSubscriber extends OrderStatusEventSubscriber {

    @Autowired
    public BakingOrderReceivedEventSubscriber(OrderService orderService, ObjectMapper objectMapper) {
        super(orderService, objectMapper, "BakingOrderReceived", OrderStatus.BAKING);
    }
}
