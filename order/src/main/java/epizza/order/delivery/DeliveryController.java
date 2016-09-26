package epizza.order.delivery;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import epizza.order.Order;
import epizza.order.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/orders/{id}/delivery")
public class DeliveryController {

    private final OrderService orderService;

    // FIXME introduce validation
    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignDelivery(@PathVariable("id") Order order, @RequestBody DeliveryJob deliveryJob) {
        orderService.assignOrder(order, deliveryJob);
        return ResponseEntity.noContent().build();
    }
}
