package epizza.delivery.order;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

@Component
public class OrderServiceListClient {

    private final Traverson orderRestClient;

    public OrderServiceListClient(@Qualifier("order") Traverson orderRestClient) {
        this.orderRestClient = orderRestClient;
    }

    public PagedResources<Order> getOrders(Pageable pageable) {
        return orderRestClient
                .follow("orders")
                .withTemplateParameters(ImmutableMap.of("page", pageable.getPageNumber()))
                .toObject(new ParameterizedTypeReference<PagedResources<Order>>() {});
    }

}
