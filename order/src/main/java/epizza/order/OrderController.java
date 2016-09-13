package epizza.order;

import static java.util.stream.Collectors.toList;

import epizza.order.catalog.Pizza;
import epizza.order.catalog.PizzaRepository;
import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@ExposesResourceFor(Order.class)
@CrossOrigin(exposedHeaders = "Location", value = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final EntityLinks entityLinks;
    private final PizzaRepository pizzaRepository;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;
    private final Validator validator;

    @InitBinder
    public void registerValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody @Valid CartPayload cart, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RepositoryConstraintViolationException(bindingResult);
        }

        Order order = new Order();
        order.setDeliveryAddress(cart.getDeliveryAddress());
        order.setComment(cart.getComment());
        order.setOrderItems(cart.getLineItems().stream().map(toLineItem()).collect(toList()));
        order = orderService.create(order);
        URI location = entityLinks.linkForSingleResource(Order.class, order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/orders/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(path = "/orders/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(path = "/orders", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseBody
    public PagedResources<Resource<Order>> getAll(Pageable pageable) {
        return pagedResourcesAssembler.toResource(orderService.findUnassigned(pageable));
    }

    private static void checkValidationResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RepositoryConstraintViolationException(bindingResult);
        }
    }

    private Function<LineItemPayload, OrderItem> toLineItem() {
        return lineItemPayload -> {
            URI pizzaUri = lineItemPayload.getPizza();
            Pizza pizza = pizzaRepository.findByUri(pizzaUri)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Unknown pizza %s", pizzaUri.toString())));
            return OrderItem.builder()
                    .pizza(pizza)
                    .quantity(lineItemPayload.getQuantity())
                    .build();
        };
    }
}
