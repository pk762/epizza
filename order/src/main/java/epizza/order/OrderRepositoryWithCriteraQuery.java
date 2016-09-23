package epizza.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryWithCriteraQuery {
    Page<Order> findUnassigned(Pageable pageable);

    Long countUnassigned();
}
