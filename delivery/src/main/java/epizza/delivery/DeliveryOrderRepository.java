package epizza.delivery;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.net.URI;

public interface DeliveryOrderRepository extends PagingAndSortingRepository<DeliveryOrder, Long> {

    DeliveryOrder findByOrderLink(URI orderLink);
}
