package epizza.order.checkout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>
// SCHNIPP
        , QueryDslPredicateExecutor<Order>
// SCHNAPP
{

}
