package epizza.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public Order create(Order order) {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("order does not have items");
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
        return orderRepository.findOrdersByDeliveryBoyIsNull(pageable);
    }

    public void assignDelivery(Order order, DeliveryJob job) {
        if(order.getDeliveryBoy() != null) {
            throw new OrderAssignedException();
        }

        order.setDeliveryBoy(job.getDeliveryBoy());
        order.setEstimatedTimeOfDelivery(job.getEstimatedTimeOfDelivery());
        orderRepository.saveAndFlush(order);
    }
}
