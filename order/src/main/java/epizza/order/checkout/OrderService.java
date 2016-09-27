package epizza.order.checkout;

import com.google.common.annotations.VisibleForTesting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import epizza.order.delivery.DeliveryJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.data.jpa.domain.Specifications.where;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderEventPublisher orderEventPublisher;

    public Order create(Order order) throws OrderItemsEmptyException {
        if (order.getOrderItems().isEmpty()) {
            throw new OrderItemsEmptyException("order does not have any items");
        }

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
                return orderRepository.findAll(where(OrderRepositorySpecifications.deliveryBoyIsNull()), pageable);
            case QUERY_BY_EXAMPLE:
                return orderRepository.findAll(OrderRepositoryExamples.whereDeliveryBoyIsNull(), pageable);
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

    public Order assignOrder(Order order, DeliveryJob deliveryJob) throws OrderAssignedException {
        if (order.getDeliveryBoy() != null) {
            throw new OrderAssignedException(String.format("Order '%d' is already assigned to '%s'", order.getId(), order.getDeliveryBoy()));
        }
        log.info("Assigning delivery job '{}' to order number {}", deliveryJob, order.getId());
        order.setDeliveryBoy(deliveryJob.getDeliveryBoy());
        order.setEstimatedTimeOfDelivery(deliveryJob.getEstimatedTimeOfDelivery());
        return update(order);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Order Items is empty.")
    public static class OrderItemsEmptyException extends RuntimeException {
        public OrderItemsEmptyException(String message) {
            super(message);
        }
    }

    @ResponseStatus(code = HttpStatus.CONFLICT, reason = "Order already assigned.")
    public static class OrderAssignedException extends RuntimeException {
        public OrderAssignedException(String message) {
            super(message);
        }
    }
}
