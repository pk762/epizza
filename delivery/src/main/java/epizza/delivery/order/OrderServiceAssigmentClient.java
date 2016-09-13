package epizza.delivery.order;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OrderServiceAssigmentClient {

    private final RestTemplate restTemplate;

    public void assignMyselfToOrder(URI orderUri, DeliveryJob job) {
        restTemplate.postForEntity(orderUri, job, Void.class);
    }
}
