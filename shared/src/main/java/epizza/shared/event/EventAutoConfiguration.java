package epizza.shared.event;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EnableRabbit.class)
public class EventAutoConfiguration {

    public static final String RND_EVENTS = "rnd.events";

    @Bean
    @ConditionalOnMissingBean
    public FanoutExchange eventsExchange() {
        final boolean durable = true;
        final boolean autoDelete = false;
        return new FanoutExchange(RND_EVENTS, durable, autoDelete);
    }

    @Bean
    @ConditionalOnMissingBean(RabbitTemplate.class)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setExchange(eventsExchange().getName());
        return rabbitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public EventPublisher eventPublisher() {
        return new EventPublisher();
    }
}
