package epizza.order;

import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;

import java.net.URI;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@RequestMapping(value = "/orders")
@ExposesResourceFor(Order.class)
@CrossOrigin(exposedHeaders = "Location", value = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final EntityLinks entityLinks;
    private final PizzaRepository pizzaRepository;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody @Valid CartPayload cartPayload) {
        Order order = new Order();
        order.setDeliveryAddress(cartPayload.getDeliveryAddress());
        order.setComment(cartPayload.getComment());
        order.setOrderItems(cartPayload.getLineItems().stream().map(toLineItem()).collect(toList()));
        order = orderService.create(order);
        URI location = entityLinks.linkForSingleResource(Order.class, order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseBody
    public PagedResources<Resource<Order>> getAll(Pageable pageable) {
        return pagedResourcesAssembler.toResource(orderService.getAll(pageable));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PersistentEntityResource> get(@PathVariable Long id, PersistentEntityResourceAssembler assembler) {
        return orderService.getOrder(id)
                .map(assembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
