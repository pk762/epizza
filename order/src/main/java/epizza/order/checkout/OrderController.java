package epizza.order.checkout;

import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

@RepositoryRestController
@ExposesResourceFor(Order.class)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final EntityLinks entityLinks;

    private final OrderItemConverter toOrderItem;

    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody @Valid CartPayload cart) {
        List<OrderItem> orderItems = cart.getLineItems().stream()
                .map(toOrderItem)
                .collect(Collectors.toList());

        Order order = new Order();
        order.setDeliveryAddress(cart.getDeliveryAddress());
        order.setComment(cart.getComment());
        order.setOrderItems(orderItems);
        order = orderService.create(order);

        URI location = entityLinks.linkForSingleResource(Order.class, order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/orders", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseBody
    public PagedResources<Resource<Order>> getAll(Pageable pageable) {
        return pagedResourcesAssembler.toResource(orderService.getAll(pageable));
    }

    @RequestMapping(path = "/orders/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(path = "/orders/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
