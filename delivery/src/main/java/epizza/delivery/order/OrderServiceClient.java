package epizza.delivery.order;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    private final URI baseUri;

    public void assignMyselfToOrder(Integer orderId, DeliveryJob job) {
        restTemplate.postForEntity(UriComponentsBuilder.fromUri(baseUri).path("orders/" + orderId + "/delivery").toUriString(), job, Void.class);
    }

    public PagedResources<Order> getOrders(Pageable pageable) {
        return restTemplate.exchange(
                UriComponentsBuilder.fromUri(baseUri).path("/orders").build().toUri(), 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Order>>() {}).getBody();
    }
}
