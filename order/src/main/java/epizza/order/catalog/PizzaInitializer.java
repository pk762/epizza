package epizza.order.catalog;

import lombok.AllArgsConstructor;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PizzaInitializer implements InitializingBean {

    private final PizzaRepository pizzaRepository;

    @Override
    @Transactional
    public void afterPropertiesSet() {
        pizzaRepository.save(salamiPizza());
        pizzaRepository.save(hawaiiPizza());
        pizzaRepository.save(peanutButterPizza());
        pizzaRepository.save(tonnoPizza());
    }

    private static Pizza hawaiiPizza() {
        return Pizza.builder()
                .name("Pizza Hawaii")
                .description("The exotic among the classics - Pizza Hawaii")
                .topping(Topping.CHEESE)
                .topping(Topping.HAM)
                .topping(Topping.PINEAPPLE)
                .imageUrl("/img/hawaii.jpg")
                .price(Money.of(9.90, "EUR"))
                .build();
    }

    private static Pizza salamiPizza() {
        return Pizza.builder()
                .name("Pizza Salami")
                .description("The classic - Pizza Salami")
                .topping(Topping.CHEESE)
                .topping(Topping.SALAMI)
                .imageUrl("/img/salami.jpg")
                .price(Money.of(8.90, "EUR"))
                .build();
    }

    private static Pizza peanutButterPizza() {
        return Pizza.builder()
                .name("Chicken Pizza with Peanut Butter")
                .description("Take this!")
                .topping(Topping.CHEESE)
                .topping(Topping.PEANUT_BUTTER)
                .topping(Topping.CHICKEN)
                .imageUrl("/img/chicken.jpg")
                .price(Money.of(10.90, "EUR"))
                .build();
    }

    private static Pizza tonnoPizza() {
        return Pizza.builder()
                .name("Pizza Tonno")
                .description("Tuna - no dolphins - promise!")
                .topping(Topping.CHEESE)
                .topping(Topping.TUNA)
                .imageUrl("/img/tonno.png")
                .price(Money.of(8.90, "EUR"))
                .build();
    }
}
