package epizza.order;

import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.ExampleMatcher.matching;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

class OrderRepositoryExamples {
    static Example<Order> deliveryBoyIsNullExample() {
        String[] ignoredPaths = Stream.of(Order.class.getDeclaredFields()) //
                .map(Field::getName) //
                .filter((fieldName) -> !"deliveryBoy".equals(fieldName)) //
                .collect(toSet()) //
                .toArray(new String[] {});

        ExampleMatcher matcher = matching() //
                .withIncludeNullValues() //
                .withIgnorePaths(ignoredPaths);
        return Example.of(new Order(), matcher);
    }
}
