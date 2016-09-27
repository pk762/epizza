package epizza.order.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends OrderRepositoryExtension, OrderRepositoryWithNamedQuery, OrderRepositoryWithCriteraQuery, //
        JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>, QueryDslPredicateExecutor<Order> {
}
