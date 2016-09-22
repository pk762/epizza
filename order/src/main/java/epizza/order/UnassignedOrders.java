package epizza.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UnassignedOrders {
    Page<Order> find(Pageable pageable);

    Long count();
}
