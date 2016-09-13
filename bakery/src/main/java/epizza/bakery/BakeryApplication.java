package epizza.bakery;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@EntityScan
@EnableAsync
@SpringBootApplication
public class BakeryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BakeryApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor bakeryThreadPoolTaskExecutor() {
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
}
