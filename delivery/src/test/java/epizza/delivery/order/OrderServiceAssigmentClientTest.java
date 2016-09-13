package epizza.delivery.order;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import epizza.delivery.DeliveryApplication;

public class OrderServiceAssigmentClientTest {

    @Rule
    public WireMockRule wiremockServer = new WireMockRule();

    @Test
    public void test() {
        // GIVEN
        String wiremockUrl = "http://localhost:" + wiremockServer.port();
        stubFor(post(urlEqualTo("/orders/1/delivery")).willReturn(aResponse().withStatus(204)));
    
        RestTemplate restTemplate = new DeliveryApplication().restTemplate(Arrays.asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8")), 
                new MappingJackson2HttpMessageConverter()
                ));
        OrderServiceAssigmentClient client = new DeliveryApplication().assignmentClient(restTemplate, URI.create(wiremockUrl));

        // WHEN
        client.assignMyselfToOrder(1, new DeliveryJob("Joe Slo", LocalDateTime.now().plusMinutes(120)));

        // THEN
        verify(exactly(1), postRequestedFor(urlEqualTo("/orders/1/delivery")));
    }

}
