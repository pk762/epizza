package epizza.delivery.order;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceAssigmentClient {

    private final RestTemplate restTemplate;

    private final UriComponentsBuilder baseUri;

    public void assignMyselfToOrder(String orderId, DeliveryJob job) {
        restTemplate.postForEntity(baseUri.path("orders/" + orderId + "/delivery").toUriString(), job, Void.class);
    }
}
