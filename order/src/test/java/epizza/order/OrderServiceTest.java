package epizza.order;

import static org.assertj.core.api.BDDAssertions.then;

import epizza.order.catalog.Pizza;
import epizza.order.catalog.PizzaRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

@Transactional
@OrderApplicationTest(properties = {
        "spring.jpa.properties.hibernate.show_sql=true",
        "spring.jpa.properties.hibernate.format_sql=true",
        "spring.jpa.properties.hibernate.use_sql_comments=false"
})
@RunWith(SpringRunner.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private PizzaRepository pizzaRepository;

    private Order order;

    @Before
    public void createOrder() {
        order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setDeliveryAddress(address());
        order.setOrderItems(ImmutableList.of(orderItem()));
        order = orderService.create(order);
    }

    @After
    public void deleteOrder() {
        orderRepository.deleteAll();
    }

    @Test
    public void should_find_unassigned_orders() {
        // GIVEN

        // WHEN
        Page<Order> unassignedOrders = orderService.findUnassigned(new PageRequest(0, 20));

        // THEN
        then(unassignedOrders.getContent()).extracting(Order::getId).containsOnly(order.getId());
    }

    private OrderItem orderItem() {
        return OrderItem.builder()
                .pizza(pizza())
                .quantity(1)
                .build();
    }

    private Pizza pizza() {
        return pizzaRepository.findOne(1L);
    }

    private Address address() {
        return Address.builder()
                .firstname("Joe")
                .lastname("Developer")
                .street("Street")
                .city("City")
                .postalCode("12345")
                .telephone("555-shoe")
                .build();
    }
}
