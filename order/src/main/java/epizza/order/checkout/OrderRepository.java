package epizza.order.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends
        OrderRepositoryWithNamedQuery
// SCHNIPP
        , OrderRepositoryExtension
        , JpaRepository<Order, Long>
        , QueryDslPredicateExecutor<Order>
        , OrderRepositoryWithCriteraQuery
        , JpaSpecificationExecutor<Order>
// SCHNAPP
{

}
