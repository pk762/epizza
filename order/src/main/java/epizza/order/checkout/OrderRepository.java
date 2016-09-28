package epizza.order.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends
        OrderRepositoryWithNamedQuery
        , JpaRepository<Order, Long>
// SCHNIPP
        , OrderRepositoryExtension
        , QueryDslPredicateExecutor<Order>
        , OrderRepositoryWithCriteraQuery
        , JpaSpecificationExecutor<Order>
// SCHNAPP
{

}
