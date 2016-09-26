package epizza.order;

import lombok.RequiredArgsConstructor;

import org.junit.rules.ExternalResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbCleanupRule extends ExternalResource {

    private final OrderRepository orderRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    protected void before() throws Throwable {
        orderRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE PIZZA_ORDER ALTER COLUMN id RESTART WITH 1");
    }
}
