package epizza.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.jackson.datatype.money.FastMoneyFactory;
import org.zalando.jackson.datatype.money.MoneyModule;

import java.util.Locale;

import epizza.order.checkout.Order;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
public class JsonConfiguration implements Jackson2ObjectMapperBuilderCustomizer, Ordered {

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Bean
    public MoneyModule moneyModule() {
        return new MoneyModule(new FastMoneyFactory());
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder //
                .locale(Locale.ENGLISH) //
                .timeZone("UTC") //
                .indentOutput(true) //
                .serializationInclusion(ALWAYS) //
                .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS, FAIL_ON_UNKNOWN_PROPERTIES) //
                .mixIn(Order.class, OrderMixin.class);
    }

    public interface OrderMixin {
        @JsonProperty("_id")
        Long getId();
    }
}
