package epizza.order;

import org.springframework.data.jpa.domain.Specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract class OrderRepositorySpecifications {
    static Specification<Order> deliveryBoyIsNull() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deliveryBoy"));
    }
}
