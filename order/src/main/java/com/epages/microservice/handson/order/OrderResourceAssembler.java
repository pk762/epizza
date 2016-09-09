package com.epages.microservice.handson.order;

import java.net.URI;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class OrderResourceAssembler extends ResourceAssemblerSupport<Order, OrderResource> {


    private final RepositoryEntityLinks repositoryEntityLinks;

    @Autowired
    public OrderResourceAssembler(RepositoryEntityLinks repositoryEntityLinks) {
        super(OrderController.class, OrderResource.class);
        this.repositoryEntityLinks = repositoryEntityLinks;
    }

    @Override
    public OrderResource toResource(Order order) {
        OrderResource orderResource = createResourceWithId(order.getId(), order);
        orderResource.setDeliveryAddress(new AddressResource(order.getDeliveryAddress()));
        orderResource.setOrderItems(
                order.getOrderItems().stream()
                .map(orderItem -> {
                    LineItemResource orderItemResource = new LineItemResource();
                    URI pizzaUri = URI.create(repositoryEntityLinks.linkToSingleResource(orderItem.getPizza()).getHref());
                    orderItemResource.setPizza(pizzaUri);
                    orderItemResource.setAmount(orderItem.getAmount());
                    orderItemResource.setPrice(orderItem.getPrice());
                    //add links
                    return orderItemResource;
                })
                .collect(Collectors.toList())
        );
        return orderResource;
    }

    @Override
    protected OrderResource instantiateResource(Order order) {
        return new OrderResource(order);
    }
}
