package epizza.order;

import com.google.common.annotations.VisibleForTesting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import epizza.order.delivery.DeliveryJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;
import static epizza.order.OrderRepositoryExamples.whereDeliveryBoyIsNull;
import static epizza.order.OrderRepositorySpecifications.deliveryBoyIsNull;
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
            throw new OrderAssignedException(String.format("Order '%d' is already assigned to '%s'", order.getId(), order.getDeliveryBoy()));
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
        return findUnassigned(pageable, OrderRepositoryQueryImplementation.NAMED_QUERY);
    }

    @VisibleForTesting
    Page<Order> findUnassigned(Pageable pageable, OrderRepositoryQueryImplementation implementation) {
        switch (implementation) {
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
                throw new IllegalStateException(String.format("Unknown query implementation '%s'", implementation));
        }
    }
}
