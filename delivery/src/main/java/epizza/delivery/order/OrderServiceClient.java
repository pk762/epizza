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
        String uri = UriComponentsBuilder.fromUri(baseUri).path("orders/" + orderId + "/delivery").toUriString();
        log.info("Assigning myself to order via Location: {}", uri);
        restTemplate.postForEntity(uri, job, Void.class);
    }

    @HystrixCommand(fallbackMethod = "hystrixFallback")
    public PagedResources<Order> getOrders() {
        URI uri = UriComponentsBuilder.fromUri(baseUri).path("/orders").build().toUri();
        log.info("Retrieving orders from Location: {}", uri);
        return restTemplate.exchange(
                uri, 
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Order>>() {}).getBody();
    }

    PagedResources<Order> hystrixFallback() {
        log.error("Hystrix said: Need to use fallback (empty list)");
        return new PagedResources<>(Collections.emptyList(), new PageMetadata(0, 0, 0));
    }
}
