package epizza.orderui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApiProperties.class)
public class OrderUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderUiApplication.class, args);
    }
}
