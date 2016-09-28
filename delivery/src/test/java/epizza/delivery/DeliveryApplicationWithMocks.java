package epizza.delivery;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@SpringBootApplication
@Import(DeliveryApplication.class)
class DeliveryApplicationWithMocks {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DeliveryApplicationWithMocks.class, args);
    }

    @Bean
    public WireMockServer wiremockServer(@Value("${orders.baseUri}") URI ordersBaseUri) {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration.options().port(ordersBaseUri.getPort())
                .fileSource(new ClasspathFileSource("wiremock/order")));
        wireMockServer.start();
        return wireMockServer;
    }
}