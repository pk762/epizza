package epizza.order;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.URI;
import java.util.ArrayList;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import lombok.SneakyThrows;
@RunWith(SpringRunner.class)
@OrderApplicationTest
public class OrderControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private OrderEventPublisher orderEventPublisher;

    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;

    private ResultActions ordersResultAction;

    private String jsonInput;

    private String pizzaSampleResponse = "{\n" +
            "  \"name\": \"Pizza Salami\",\n" +
            "  \"description\": \"The classic - Pizza Salami\",\n" +
            "  \"imageUrl\": \"http://www.sardegna-rustica.de/images/pizza.jpg\",\n" +
            "  \"price\": {\"amount\": 8.90, \"currency\": \"EUR\"}}";

    private Order order;

    @Before
    public void setupContext(){
        mockMvc = webAppContextSetup(context)
                .apply(documentationConfiguration(this.restDocumentation).uris().withPort(80))
                .build();

        mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(
                requestTo("http://localhost/pizzas/1")).
                andRespond(withSuccess(pizzaSampleResponse, MediaType.APPLICATION_JSON));

        orderRepository.deleteAll();

        reset(orderEventPublisher);
    }

    @Test
    @SneakyThrows
    public void should_create_order() {
        givenInputData();

        whenOrderCreated();

        ordersResultAction
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("/orders")))
                .andDo(document("order-create", //
                        requestFields( //
                                fieldWithPath("comment").description("delivery comment"), //
                                fieldWithPath("lineItems[].pizza").description("which pizza do you want?"), //
                                fieldWithPath("lineItems[].quantity").description("how many pizzas do you eat today?"), //
                                fieldWithPath("deliveryAddress.firstname").description("Your first name"), //
                                fieldWithPath("deliveryAddress.lastname").description("Your last name"), //
                                fieldWithPath("deliveryAddress.street").description("Your stree"), //
                                fieldWithPath("deliveryAddress.city").description("Your city"), //
                                fieldWithPath("deliveryAddress.postalCode").description("Your postal code"), //
                                fieldWithPath("deliveryAddress.telephone").description("Your telephone"), //
                                fieldWithPath("deliveryAddress.email").description("Your email address").optional() //
                )))
        ;

        verify(orderEventPublisher).sendOrderCreatedEvent(order);
    }

    @Test
    @SneakyThrows
    public void should_get_order() {
        givenExistingOrder();

        whenOrderRetrieved();

        ordersResultAction
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.status", is(order.getStatus().name())))
                .andExpect(jsonPath("$.totalPrice", notNullValue()))
                .andExpect(jsonPath("$.orderItems", hasSize(order.getOrderItems().size())))
                .andExpect(jsonPath("$.deliveryAddress.firstname", is(order.getDeliveryAddress().getFirstname())))
                .andExpect(jsonPath("$._links.self.href",
                        is(entityLinks.linkForSingleResource(Order.class, order.getId()).toUri().toString())))

                .andDo(document("order-get",
                        responseFields(
                                fieldWithPath("status").description("Order status"),
                                fieldWithPath("orderedAt").description("Order creation timestamp"),
                                fieldWithPath("totalPrice").description("Total order amount"),
                                fieldWithPath("estimatedTimeOfDelivery").description("Estimated time of delivery"),
                                fieldWithPath("deliveryBoy").description("Delivery boy"),
                                fieldWithPath("comment").description("Customer's comment"),
                                fieldWithPath("orderItems[]._links.pizza").description("Link to ordered pizza"),
                                fieldWithPath("orderItems[].quantity").description("Number of pizzas"),
                                fieldWithPath("orderItems[].price").description("Price (Currency symbol and numeric value)"),
                                fieldWithPath("deliveryAddress").description("Delivery address as POSTed when <<resources-order-create,creating an Order>>"),
                                fieldWithPath("_links").description("<<links,Links>> to other resources")
                        ))) //
        ;
    }

    @Test
    @SneakyThrows
    public void should_get_all_orders() {
        givenExistingOrder();

        whenAllOrdersRetrieved();

        ordersResultAction
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(document("orders-list",
                        responseFields(
                                fieldWithPath("_embedded").description("Current page of <<resources-order-get,Orders>>"),
                                fieldWithPath("page").description("<<paging,Paging>> information"),
                                fieldWithPath("_links").description("<<links,Links>> to other resources")
                        ))) //
        ;
    }

    @SneakyThrows
    private void whenAllOrdersRetrieved() {
        ordersResultAction = mockMvc.perform(get("/orders").accept(MediaTypes.HAL_JSON));
    }

    @SneakyThrows
    private void whenOrderRetrieved() {
        URI orderUri = entityLinks.linkForSingleResource(Order.class, order.getId()).toUri();

        ordersResultAction = mockMvc.perform(get(orderUri)
                .accept(MediaTypes.HAL_JSON))
                .andDo(print());
    }

    private void givenExistingOrder() {
        Order newOrder = new Order();
        newOrder.setComment("some comment");
        Address address = Address.builder()
                .city("Hamburg")
                .firstname("Mathias")
                .lastname("Dpunkt")
                .postalCode("22222")
                .street("Pilatuspool 2")
                .telephone("+4908154711")
                .build();
        newOrder.setDeliveryAddress(address);

        OrderItem orderItem = OrderItem.builder()
                .pizza(Pizza.builder().id(1L).price(Money.parse("EUR 1.23")).build())
                .quantity(2)
                .build();

        newOrder.addOrderItem(orderItem);

        order = orderService.create(newOrder);
    }

    @SneakyThrows
    private void whenOrderCreated() {
        ordersResultAction = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput));

        order = orderService.getAll(null).iterator().next();
    }

    @SneakyThrows
    private void givenInputData() {
        ImmutableMap<String, String> address = ImmutableMap.<String, String>builder()
                .put("firstname", "Mathias")
                .put("lastname", "Dpunkt")
                .put("street", "Somestreet 1")
                .put("city", "Hamburg")
                .put("telephone", "+49404321343")
                .put("postalCode", "22305") //
                .put("email", "your@email.address") //
                .build();

        jsonInput = objectMapper.writeValueAsString(ImmutableMap.of(
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
