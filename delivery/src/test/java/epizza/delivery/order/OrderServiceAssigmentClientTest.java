package epizza.delivery.order;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import epizza.delivery.DeliveryApplication;
import epizza.delivery.JsonConfiguration;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class OrderServiceAssigmentClientTest {

    @Rule
    public WireMockRule wiremockServer = new WireMockRule();

    @Test
    public void test() {
        // GIVEN
        String wiremockUrl = "http://localhost:" + wiremockServer.port();
        stubFor(post(urlEqualTo("/orders/1/delivery")).willReturn(aResponse().withStatus(204)));
        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
        new JsonConfiguration().customize(builder);
        RestTemplate restTemplate = new DeliveryApplication().restTemplate(Arrays.asList(new MappingJackson2HttpMessageConverter(builder.build())));
        OrderServiceAssigmentClient client = new DeliveryApplication().assignmentClient(restTemplate, URI.create(wiremockUrl));

        // WHEN
        client.assignMyselfToOrder(1, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));

        // THEN
        verify(exactly(1), postRequestedFor(urlEqualTo("/orders/1/delivery")));

        findAll(postRequestedFor(urlEqualTo("/orders/1/delivery"))).stream().forEach((n) -> System.out.println("logged request:  " + n));
    }

}
