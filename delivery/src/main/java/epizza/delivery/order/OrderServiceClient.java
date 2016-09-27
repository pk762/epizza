package epizza.delivery.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Collections;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RequiredArgsConstructor
@Slf4j
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    private final URI baseUri;

    @HystrixCommand
    public void assignMyselfToOrder(Integer orderId, DeliveryJob job) {
        restTemplate.postForEntity(UriComponentsBuilder.fromUri(baseUri).path("orders/" + orderId + "/delivery").toUriString(), job, Void.class);
    }

    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public PagedResources<Order> getOrders() {
        return restTemplate.exchange(
                UriComponentsBuilder.fromUri(baseUri).path("/orders").build().toUri(), 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Order>>() {}).getBody();
    }

    PagedResources<Order> hystrixFallback() {
        log.error("Hystrix said: Need to use fallback (empty list)");
        return new PagedResources<>(Collections.emptyList(), new PageMetadata(0, 0, 0));
    }
}
