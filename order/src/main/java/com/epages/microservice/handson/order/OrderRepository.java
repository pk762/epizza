package com.epages.microservice.handson.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = false)
public interface OrderRepository extends JpaRepository<Order, Long> {
}
