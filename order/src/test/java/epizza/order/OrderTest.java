package epizza.order;

import static org.assertj.core.api.BDDAssertions.then;

import org.javamoney.moneta.Money;
import org.junit.Test;

public class OrderTest {

    private Pizza salami = Pizza.builder()
            .name("Salami")
            .price(Money.of(9.9, "EUR"))
            .build();

    @Test
    public void should_calculate_total_price() {
        // GIVEN
        Order order = new Order();
        OrderItem orderItem = OrderItem.builder()
                .pizza(salami)
                .quantity(2)
                .build();

        // WHEN
        order.addOrderItem(orderItem);

        // THEN

        then(order.getTotalPrice()).isEqualByComparingTo(Money.of(19.8, "EUR"));
    }
}
