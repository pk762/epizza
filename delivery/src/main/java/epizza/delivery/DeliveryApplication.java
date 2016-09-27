package epizza.delivery;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import epizza.delivery.order.OrderServiceClient;

@SpringBootApplication
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public OrderServiceClient orderClient(RestTemplate restTemplate, @Value("${orders.baseUri}") URI ordersUri) {
        return new OrderServiceClient(restTemplate, ordersUri);
    }

    @Bean
    @Qualifier("order")
    public Traverson orderRestClient(@Value("${orders.baseUri}") URI ordersUri) {
        return  new Traverson(ordersUri, MediaTypes.HAL_JSON);
    }
}
