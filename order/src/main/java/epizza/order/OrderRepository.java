package epizza.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

public interface OrderRepository extends JpaRepository<Order, Long>, QueryDslPredicateExecutor<Order> {

    @RestResource(exported = false)
    Page<Order> findOrdersByDeliveryBoyIsNull(Pageable pageable);
}
