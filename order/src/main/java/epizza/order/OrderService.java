package epizza.order;

import com.google.common.annotations.VisibleForTesting;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import epizza.order.delivery.DeliveryJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.ExampleMatcher.matching;
import static org.springframework.data.jpa.domain.Specifications.where;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderEventPublisher orderEventPublisher;

    public Order create(Order order) {
        checkArgument(!order.getOrderItems().isEmpty(), "order does not have items");

        order.setOrderedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.saveAndFlush(order);

        orderEventPublisher.sendOrderCreatedEvent(savedOrder);
        log.info("order created {}", savedOrder);
        return savedOrder;
    }

    public Order update(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(Long id) {
        return Optional.ofNullable(orderRepository.findOne(id));
    }

    public Order assignOrder(Order order, DeliveryJob deliveryJob) throws OrderAssignedException {
        if (order.getDeliveryBoy() != null) {
            throw new OrderAssignedException();
        }
        log.info("Assigning delivery job '{}' to order number {}", deliveryJob, order.getId());
        order.setDeliveryBoy(deliveryJob.getDeliveryBoy());
        order.setEstimatedTimeOfDelivery(deliveryJob.getEstimatedTimeOfDelivery());
        return update(order);
    }

    public Page<Order> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }


    public Page<Order> findUnassigned(Pageable pageable) {
        return findUnassigned(pageable, QueryImplementation.NAMED_QUERY);
    }

    @VisibleForTesting
    enum QueryImplementation {
        NAMED_QUERY, // JPA Named Query
        CRITERIA_QUERY, // JPA Criteria Query
        QUERY_BY_SPECIFICATION, // Spring Data Query By Specification
        QUERY_BY_EXAMPLE, // Spring Data Query By Example
        QUERY_ANNOTATION, // Spring Data @Query Annotation
        NAMING_CONVENTION, //  Spring Data method naming convention
        QUERYDSL // Querydsl
    }

    @VisibleForTesting
    Page<Order> findUnassigned(Pageable pageable, QueryImplementation queryImplementation) {
        switch (queryImplementation) {
            case NAMED_QUERY:
                return orderRepository.findByNamedQuery(OrderRepositoryWithNamedQuery.UNASSIGNED_NAME, pageable);
            case CRITERIA_QUERY:
                return orderRepository.findUnassigned(pageable);
            case QUERY_BY_SPECIFICATION:
                return orderRepository.findAll(where(deliveryBoyIsNull()), pageable);
            case QUERY_BY_EXAMPLE:
                return orderRepository.findAll(whereDeliveryBoyIsNull(), pageable);
            case QUERYDSL:
                return orderRepository.findAll(QOrder.order.deliveryBoy.isNull(), pageable);
            case QUERY_ANNOTATION:
                return orderRepository.findByMissingDeliveryBoy(pageable);
            case NAMING_CONVENTION:
                return orderRepository.findByDeliveryBoyIsNull(pageable);
            default:
                throw new IllegalStateException(String.format("Unknown query implementation '%s'", queryImplementation));
        }
    }

    private static Specification<Order> deliveryBoyIsNull() {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deliveryBoy"));
    }

    private static Example<Order> whereDeliveryBoyIsNull() {
        String[] ignoredPaths = Stream.of(Order.class.getDeclaredFields()) //
                .map(Field::getName) //
                .filter((fieldName) -> !"deliveryBoy".equals(fieldName)) //
                .collect(toSet()) //
                .toArray(new String[]{});

        ExampleMatcher matcher = matching() //
                .withIncludeNullValues() //
                .withIgnorePaths(ignoredPaths);
        return Example.of(new Order(), matcher);
    }
}
