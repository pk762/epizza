package epizza.order.checkout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryWithNamedQuery {
    String UNASSIGNED_NAME = "Order.unassigned";

    String UNASSIGNED_QUERY = "from Order o"
// SCHNIPP
            + " where o.deliveryBoy is null"
// SCHNAPP
            ;

    String COUNT_UNASSIGNED_NAME = UNASSIGNED_NAME + ".count";

    String COUNT_UNASSIGNED_QUERY = "select count(o) from Order o"
// SCHNIPP
            + " where o.deliveryBoy is null"
// SCHNAPP
            ;

    Page<Order> findByNamedQuery(String name, Pageable pageable);

    Long countByNamedQuery(String name);
}
