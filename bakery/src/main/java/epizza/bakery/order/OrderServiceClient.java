package epizza.bakery.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    public Order getOrder(URI orderUri) {
        Order order = restTemplate.getForObject(orderUri, Order.class);
        log.info("Read order from URI {} - got {}", orderUri, order);
        return order;
    }
}
