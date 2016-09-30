package epizza.delivery.order;

import lombok.RequiredArgsConstructor;
// SCHNIPP
import lombok.extern.slf4j.Slf4j;
// SCHNAPP

import java.net.URI;
// SCHNIPP
import java.util.Collections;
// SCHNAPP

import org.springframework.hateoas.PagedResources;
// SCHNIPP
import org.springframework.hateoas.PagedResources.PageMetadata;
// SCHNAPP
import org.springframework.hateoas.mvc.TypeReferences;
// SCHNIPP
import org.springframework.http.HttpMethod;
// SCHNAPP
import org.springframework.web.client.RestTemplate;
// SCHNIPP
import org.springframework.web.util.UriComponentsBuilder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
// SCHNAPP

// SCHNIPP
@Slf4j
// SCHNAPP
@RequiredArgsConstructor
public class OrderServiceClientImpl implements OrderServiceClient {

    private final RestTemplate restTemplate;

    private final URI baseUri;

    @Override
// SCHNIPP
    @HystrixCommand
// SCHNAPP
    public void selectOrder(Integer orderId, DeliveryJob job) {
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
                new ParametrizedReturnType()).getBody();
// SCHNAPP        throw new UnsupportedOperationException();
    }

// SCHNIPP
    PagedResources<Order> hystrixFallback() {
        log.error("Hystrix said: Need to use fallback (empty list)");
        return new PagedResources<>(Collections.emptyList(), new PageMetadata(0, 0, 0));
    }
// SCHNAPP

    // That's for avoiding generics type erasure.
    private static final class ParametrizedReturnType extends TypeReferences.PagedResourcesType<Order> {}

}
