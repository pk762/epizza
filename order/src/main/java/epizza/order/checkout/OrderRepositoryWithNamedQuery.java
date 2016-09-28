package epizza.order.checkout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryWithNamedQuery {
    String UNASSIGNED_NAME = "Order.deliveryBoyIsNull";

    String UNASSIGNED_QUERY = "from Order o"
// SCHNIPP
            + " where o.deliveryBoy is null"
// SCHNAPP
            ;

    Page<Order> findByNamedQuery(String name, Pageable pageable);
}
