package epizza.order;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import epizza.order.catalog.Pizza;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CouponTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @Mock
    private CouponRepository couponRepository;

    @Test
    public void should_apply_known_coupon() {
        Coupon coupon = Coupon.builder()
                .code("5EURO")
                .value(Money.of(5.0, "EUR"))
                .build();
        given(couponRepository.findOne("5EURO")).willReturn(coupon);

        Order order = new Order();
        order.addOrderItem(
                OrderItem.builder()
                .pizza(
                        Pizza.builder()
                        .price(Money.of(15.0, "EUR"))
                        .build()
                ).quantity(1)
                .build()
        );
        given(orderRepository.saveAndFlush(Mockito.any())).willReturn(order);

        Order reducedOrder = orderService.create(order, "5EURO");


        then(reducedOrder.getTotalPrice()).isEqualByComparingTo(Money.of(10.0, "EUR"));


    }
}
