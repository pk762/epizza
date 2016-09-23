package epizza.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends OrderRepositoryExtension, OrderRepositoryWithCriteraQuery, //
        JpaRepository<Order, Long>, JpaSpecificationExecutor<Order>, QueryDslPredicateExecutor<Order> {
}
