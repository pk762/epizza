package com.epages.microservice.handson.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

import com.google.common.collect.Lists;

@Getter
@Setter
@NoArgsConstructor
public class OrderResource extends ResourceSupport {

    private OrderStatus status;

    private LocalDateTime created;

    private LocalDateTime estimatedTimeOfDelivery;

    private MonetaryAmount totalPrice;

    @Valid
    private List<LineItemResource> orderItems = Lists.newArrayList();

    private String comment;

    @Valid
    @NotNull
    private AddressResource deliveryAddress;

    public OrderResource(Order order) {
        this.status = order.getStatus();
        this.created = order.getOrderedAt();
        this.totalPrice = order.getTotalPrice();
        this.estimatedTimeOfDelivery = order.getEstimatedTimeOfDelivery();
    }
}
