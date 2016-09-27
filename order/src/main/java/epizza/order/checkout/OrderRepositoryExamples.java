package epizza.order.checkout;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.springframework.data.domain.ExampleMatcher.matching;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract class OrderRepositoryExamples {
    static Example<Order> whereDeliveryBoyIsNull() {
        String[] ignoredPaths = Stream.of(Order.class.getDeclaredFields()) //
                .map(Field::getName) //
                .filter((fieldName) -> !"deliveryBoy".equals(fieldName)) //
                .collect(Collectors.toSet()) //
                .toArray(new String[]{});

        ExampleMatcher matcher = matching() //
                .withIncludeNullValues() //
                .withIgnorePaths(ignoredPaths);
        return Example.of(new Order(), matcher);
    }
}
