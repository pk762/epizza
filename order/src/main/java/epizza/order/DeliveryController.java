package epizza.order;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(path="/orders/{id}/delivery")
@RestController
public class DeliveryController {

    private final OrderService orderService;

    @RequestMapping(path="", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignDelivery(@PathVariable("id") Order order, @RequestBody @Valid DeliveryJob deliveryJob) {
        orderService.assignOrder(order, deliveryJob);
        return ResponseEntity.noContent().build();
    }
}
