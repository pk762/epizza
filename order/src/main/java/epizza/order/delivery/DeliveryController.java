package epizza.order.delivery;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import epizza.order.Order;
import epizza.order.OrderService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path="/orders/{id}/delivery")
@RestController
public class DeliveryController {

    private final OrderService orderService;

    private final Validator validator;

    @InitBinder
    public void registerValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(path="", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> assignDelivery(@PathVariable("id") Order order, @RequestBody @Valid DeliveryJob deliveryJob, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RepositoryConstraintViolationException(bindingResult);
        }

        orderService.assignOrder(order, deliveryJob);
        return ResponseEntity.noContent().build();
    }
}
