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

// SCHNIPP
@RequiredArgsConstructor
@Slf4j
// SCHNAPP
public class OrderServiceClientImpl implements OrderServiceClient {

// SCHNIPP
    private final RestTemplate restTemplate;

    private final URI baseUri;
// SCHNAPP    public OrderServiceClientImpl(RestTemplate restTemplate, URI baseUri) {}

    @Override
// SCHNIPP
    @HystrixCommand
// SCHNAPP
    public void assignMyselfToOrder(Integer orderId, DeliveryJob job) {
// SCHNIPP
        String uri = UriComponentsBuilder.fromUri(baseUri).path("orders/" + orderId + "/delivery").toUriString();
        log.info("Assigning myself to order via Location: {}", uri);
        restTemplate.postForEntity(uri, job, Void.class);
// SCHNAPP        throw new UnsupportedOperationException();
    }

    @Override
// SCHNIPP
    @HystrixCommand(fallbackMethod = "hystrixFallback")
// SCHNAPP
    public PagedResources<Order> getOrders() {
// SCHNIPP
        URI uri = UriComponentsBuilder.fromUri(baseUri).path("/orders").build().toUri();
        log.info("Retrieving orders from Location: {}", uri);
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedResources<Order>>() {}).getBody();
// SCHNAPP        throw new UnsupportedOperationException();
    }

// SCHNIPP
    PagedResources<Order> hystrixFallback() {
        log.error("Hystrix said: Need to use fallback (empty list)");
        return new PagedResources<>(Collections.emptyList(), new PageMetadata(0, 0, 0));
    }
// SCHNAPP

}
