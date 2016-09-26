package epizza.order;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Function;

import epizza.order.catalog.Pizza;
import epizza.order.catalog.PizzaRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderItemConverter implements Function<LineItemPayload, OrderItem> {

    private final PizzaRepository pizzaRepository;

    @Override
    public OrderItem apply(LineItemPayload lineItem) {
        Pizza pizza = findKnownPizza(lineItem.getPizza());
        return OrderItem.builder()
                .pizza(pizza)
                .quantity(lineItem.getQuantity())
                .build();
    }

    private Pizza findKnownPizza(URI pizzaUri) {
        return pizzaRepository.findByUri(pizzaUri)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Unknown pizza %s", pizzaUri.toString())));
    }
}
