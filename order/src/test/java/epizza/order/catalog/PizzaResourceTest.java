package epizza.order.catalog;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import epizza.order.OrderApplicationTest;
import lombok.SneakyThrows;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@OrderApplicationTest
public class PizzaResourceTest {

    @Autowired
    private WebApplicationContext context;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private MockMvc mockMvc;

    private ResultActions resultAction;

    private String collectionUri;

    private String singleItemUri;

    @Autowired
    private EntityLinks entityLinks;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation).uris().withPort(80))
                .build();
        collectionUri = entityLinks.linkToCollectionResource(Pizza.class).expand().getHref();
        singleItemUri = entityLinks.linkToSingleResource(Pizza.class, "1").getHref();
    }

    @Test
    @SneakyThrows
    public void should_get_all_pizzas() {
        givenExistingPizzas();
        whenAllPizzasRetrieved();

        resultAction
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(document("pizzas-list",
                        responseFields(
                                fieldWithPath("_embedded").description("Embedded list of <<resources-pizza-get, pizzas>>."),
                                fieldWithPath("page").description("Paging information"),
                                fieldWithPath("_links").description("<<links, Links>> to other resources.")
                        )));
    }

    @Test
    @SneakyThrows
    public void should_get_one_pizza() {
        givenExistingPizzas();
        whenPizzaRetrieved();

        resultAction
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(document("pizza-get",
                        responseFields(
                                fieldWithPath("name").description("Pizza name."),
                                fieldWithPath("description").description("Pizza details."),
                                fieldWithPath("imageUrl").description("Image."),
                                fieldWithPath("price").description("pricate (Currency symbol and numeric value)."),
                                fieldWithPath("toppings[]").description("List of toppings."),
                                fieldWithPath("_links").description("<<links, Links>> to other resources.")
                        )));
    }

    @SneakyThrows
    private void whenAllPizzasRetrieved() {
        resultAction = mockMvc.perform(get(collectionUri)
                .accept(MediaTypes.HAL_JSON));
    }

    @SneakyThrows
    private void whenPizzaRetrieved() {
        resultAction = mockMvc.perform(get(singleItemUri)
                .accept(MediaTypes.HAL_JSON));
    }

    private void givenExistingPizzas() {
    }
}
