package epizza.shared.json;


import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.zalando.jackson.datatype.money.MoneyModule;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class JsonAutoConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    @Autowired
    private ParameterNamesModule parameterNamesModule;

    @Bean
    @ConditionalOnMissingBean(MoneyModule.class)
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder //
        .locale(Locale.ENGLISH) //
        .timeZone("UTC") //
        .indentOutput(true) //
        .serializationInclusion(ALWAYS) //
        .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS, FAIL_ON_UNKNOWN_PROPERTIES);

        jacksonObjectMapperBuilder.modulesToInstall(moneyModule(), parameterNamesModule);
    }


}
