package epizza.order.checkout;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

public interface OrderRepositoryExtension {

    @RestResource(exported = false)
    @Query(OrderRepositoryWithNamedQuery.UNASSIGNED_QUERY)
    Page<Order> findByMissingDeliveryBoy(Pageable pageable);

    @RestResource(exported = false)
    Page<Order> findByDeliveryBoyIsNull(Pageable pageable);
}
