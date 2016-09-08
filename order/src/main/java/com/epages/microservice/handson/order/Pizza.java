package com.epages.microservice.handson.order;

import javax.money.MonetaryAmount;
import javax.validation.constraints.NotNull;

public class Pizza {

    @NotNull
    private String name;

    @NotNull
    private MonetaryAmount price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public void setPrice(MonetaryAmount price) {
        this.price = price;
    }
}
