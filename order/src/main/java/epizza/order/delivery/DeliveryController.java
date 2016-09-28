package epizza.order.delivery;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import epizza.order.checkout.Order;
import epizza.order.checkout.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/orders/{id}/delivery")
public class DeliveryController {

    private final OrderService orderService;

    // FIXME introduce validation
    @RequestMapping(path = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignDelivery(@PathVariable("id") Order order, @RequestBody @Valid DeliveryJob deliveryJob) {
        orderService.assignOrder(order, deliveryJob);
        return ResponseEntity.noContent().build();
    }
}
