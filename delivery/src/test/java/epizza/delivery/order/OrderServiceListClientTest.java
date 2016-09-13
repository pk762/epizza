package epizza.delivery.order;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.google.common.collect.ImmutableMap.of;

import java.net.URI;

import org.assertj.core.api.BDDAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.ImmutableList;

import epizza.delivery.DeliveryApplication;
import lombok.SneakyThrows;

public class OrderServiceListClientTest {

    @Rule
    public WireMockRule wiremockServer = new WireMockRule();

    @Test
    @SneakyThrows
    public void test() {
        String wiremockUrl = "http://localhost:" + wiremockServer.port();
        stubFor(get(urlEqualTo("/")).willReturn(aResponse()
                .withHeader("Content-Type", "application/hal+json")
                .withBody(new ObjectMapper().writeValueAsString(of(
                        "_links", of(
                            "orders", of(
                                "href", wiremockUrl + "/orders{?page,size,sort}",
                                "templated", true
                                )
                            )
                        )
        ))));
        stubFor(get(urlMatching("/orders.*")).willReturn(aResponse()
                .withHeader("Content-Type", "application/hal+json")
                .withBody(new ObjectMapper().writeValueAsString(of(
                        "_embedded", of(
                            "orders", ImmutableList.of(
                                    of(
                                        "deliveryAddress", of(
                                                "firstname", "Mathias",
                                                "lastname", "Dpunkt",
                                                "street", "Pilatuspool 2",
                                                "city", "Hamburg",
                                                "postalCode", "22222"
                                        ),
                                        "_links", of(
                                            "self", of(
                                                "href", wiremockUrl + "/orders/1"
                                            )
                                        )
                                    )
                                )
                            )
                        )
        ))));

        // GIVEN
        OrderServiceListClient client = new OrderServiceListClient(new DeliveryApplication().orderRestClient(URI.create(wiremockUrl)));

        // WHEN
        PagedResources<Order> orders = client.getOrders(new PageRequest(0, 10));

        // THEN
        BDDAssertions.then(orders.getContent())
        .hasSize(1)
        .contains(new Order().withLink(new Link(wiremockUrl + "/orders/1", "self")))
        ;

    }

}
