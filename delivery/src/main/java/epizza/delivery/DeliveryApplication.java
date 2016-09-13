package epizza.delivery;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EntityScan
@EnableAsync
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

    @Bean
    ThreadPoolTaskExecutor deliveryThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(2);
        threadPoolTaskExecutor.setQueueCapacity(1000);

        return threadPoolTaskExecutor;
    }

    @Bean
    public RestTemplate restTemplate(List<HttpMessageConverter<?>> messageConverters) {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(5000);
        httpRequestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    @Bean
    @Qualifier("order")
    public Traverson orderRestClient(@Value("${orders.baseUri}") URI ordersUri) {
        return  new Traverson(ordersUri, MediaTypes.HAL_JSON);
    }
}
