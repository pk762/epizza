package epizza.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported = true)
public interface OrderRepositoryExtension {
    @Query("from Order o where o.deliveryBoy is null")
    Page<Order> findUnassigned(Pageable pageable);

    Page<Order> findByDeliveryBoyIsNull(Pageable pageable);
}
