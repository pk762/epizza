package epizza.order.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import epizza.order.checkout.OrderService;

@Component
public class BakingFinishedEventSubscriber extends AbstractOrderStatusEventSubscriber {

    @Autowired
    public BakingFinishedEventSubscriber(ObjectMapper objectMapper, OrderService orderService) {
        super(orderService, objectMapper, "BakingFinished", OrderStatus.DELIVERING);
    }
}
