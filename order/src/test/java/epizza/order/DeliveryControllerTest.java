package epizza.order;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.hal.HalLinkDiscoverer;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.List;

import lombok.SneakyThrows;

import static com.epages.restdocs.WireMockDocumentation.wiremockJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@OrderApplicationTest
public class DeliveryControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Rule
    @Autowired
    public DbCleanupRule dbCleanupRule;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderEventPublisher orderEventPublisher;

    private MockMvc mockMvc;

    private ResultActions resultAction;

    private URI orderUri;

    @Before
    public void setupContext(){
        mockMvc = webAppContextSetup(context)
                .apply(documentationConfiguration(this.restDocumentation).uris().withPort(80))
                .build();
    }

    @Test
    public void should_assign_delivery_job() {
        // GIVEN
        givenExistingOrder();

        // WHEN
        whenDeliveryJobIsCreated();

        // THEN
        thenOrderShouldHaveDeliveryBoy();
    }

    @SneakyThrows
    private void givenExistingOrder() {
        resultAction = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderInputData()));
    }

    @SneakyThrows
    private void whenDeliveryJobIsCreated() {
        orderUri = URI.create(resultAction.andReturn().getResponse().getHeader("Location"));
        resultAction = mockMvc.perform(get(orderUri));

        String jsonResponse = resultAction.andReturn().getResponse().getContentAsString();
        HalLinkDiscoverer linkDiscoverer = new HalLinkDiscoverer();
        List<Link> deliveryLinks = linkDiscoverer.findLinksWithRel("delivery", jsonResponse);
        assertThat(deliveryLinks).hasSize(1);

        resultAction = mockMvc.perform(
                post(deliveryLinks.get(0).getHref())
                .contentType(MediaType.APPLICATION_JSON)
                .content(deliveryInputData()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(document("delivery-create", requestFields(
                        fieldWithPath("deliveryBoy").description("Delivery boy"),
                        fieldWithPath("estimatedTimeOfDelivery").description("Estimated delivery time")
                        )
                        , wiremockJson()
                        ));
    }

    @SneakyThrows
    private void thenOrderShouldHaveDeliveryBoy() {
        resultAction = mockMvc.perform(get(orderUri))
                .andDo(print())
                .andExpect(jsonPath("$.deliveryBoy", is("Fred Firestove")))
                .andExpect(jsonPath("$.estimatedTimeOfDelivery", is("2016-09-14T20:05:00")))
                ;
    }

    @SneakyThrows
    private String deliveryInputData() {
        return new ObjectMapper().writeValueAsString(
                ImmutableMap.of(
                "deliveryBoy" , "Fred Firestove",
                "estimatedTimeOfDelivery", "2016-09-14T20:05:00"
            )
        );
    }
    
    @SneakyThrows
    private String orderInputData() {
        ImmutableMap<String, String> address = ImmutableMap.<String, String>builder()
                .put("firstname", "Mathias")
                .put("lastname", "Dpunkt")
                .put("street", "Somestreet 1")
                .put("city", "Hamburg")
                .put("telephone", "+49404321343")
                .put("postalCode", "22305") //
                .put("email", "your@email.address") //
                .build();

        return new ObjectMapper().writeValueAsString(ImmutableMap.of(
                "comment", "Some comment",
                "deliveryAddress", address,
                "lineItems", ImmutableList.of(ImmutableMap.of(
                                "quantity", 1,
                                "pizza", "http://localhost/pizzas/1"
                        )
                )
        ));
    }
}
