package epizza.order;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@RepositoryRestController
@RequestMapping("/orders")
@ExposesResourceFor(Order.class)
@CrossOrigin(exposedHeaders = "Location", value = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;
    private final EntityLinks entityLinks;
    private final PizzaRepository pizzaRepository;

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Optional<Order> get(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Page<Order> getAll(Pageable pageable, PagedResourcesAssembler<Order> pagedResourcesAssembler) {
        return orderService.getAll(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@RequestBody @Valid OrderResource orderResource) {
        Order order = new Order();
        order.setDeliveryAddress(orderResource.getDeliveryAddress().toEntity());
        order.setComment(orderResource.getComment());
        order.setOrderItems(orderResource.getOrderItems().stream().map(toLineItem()).collect(toList()));
        order = orderService.create(order);
        URI location = entityLinks.linkForSingleResource(Order.class, order.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    private Function<LineItemResource, LineItem> toLineItem() {
        return lineItemResource -> {
            epizza.order.Pizza pizza = pizzaRepository.findByUri(lineItemResource.getPizza())
                    .orElseThrow(() -> new ResourceNotFoundException("Unknown pizza " + lineItemResource.getPizza().toString()));
            return new epizza.order.LineItem(pizza, lineItemResource.getAmount());
        };
    }
}
