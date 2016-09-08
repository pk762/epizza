package com.epages.microservice.handson.delivery;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@DeliveryApplicationTest
public class DeliveryOrderControllerTest {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private WebApplicationContext context;

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets");

    private DeliveryOrder deliveryOrder;

    private MockMvc mockMvc;

    private String ordersUri;

    private ResultActions orderResultActions;

    @Before
    public void setupContext() {
        mockMvc = webAppContextSetup(context)
                .apply(documentationConfiguration(this.restDocumentation).uris().withPort(80))
                .build();

        ordersUri = entityLinks.linkToCollectionResource(DeliveryOrder.class).expand().getHref();
    }

    @After
    public  void cleanup() {
        deliveryOrderRepository.deleteAll();
    }

    @Test
    public void should_get_all_orders() throws Exception {
        givenDeliveryOrder();

        whenAllOrdersRetrieved();

        orderResultActions
                //.andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$._embedded.deliveryOrders", hasSize(1)))
                .andExpect(jsonPath("$._embedded.deliveryOrders[0].deliveryOrderState", is(DeliveryOrderState.IN_PROGRESS.name())))
                .andDo(document("deliveryorders-list", //
                        responseFields(
                                fieldWithPath("_links").description("<<links,Links>> to other resources"),
                                fieldWithPath("_embedded").description("Embedded <<resources-deliveryorder-get,Delivery Orders>>"),
                                fieldWithPath("page").description("<<paging,Paging>> information")
                )))
        ;
    }

    @Test
    public void should_get_order_by_link() throws Exception {
        givenDeliveryOrder();

        whenOrderRetrievedByLink();

        orderResultActions
                //.andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.orderLink", is(deliveryOrder.getOrderLink().toString())))
                .andExpect(jsonPath("$.deliveryOrderState", is(deliveryOrder.getDeliveryOrderState().name())))
        ;
    }

    @Test
    public void should_get_order() throws Exception {
        givenDeliveryOrder();

        whenOrderRetrieved();

        orderResultActions
                //.andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.orderLink", is(deliveryOrder.getOrderLink().toString())))
                .andExpect(jsonPath("$.deliveryOrderState", is(deliveryOrder.getDeliveryOrderState().name())))
                .andDo(document("deliveryorder-get", //
                        responseFields(
                                fieldWithPath("orderLink").description("order link"),
                                fieldWithPath("deliveryOrderState").description("delivery order status: " 
                                + DeliveryOrderState.QUEUED.toString() + ", " + DeliveryOrderState.IN_PROGRESS.toString() + ", " + DeliveryOrderState.DONE.toString())
                    )))
        ;
    }

    private void whenOrderRetrieved() throws Exception {
        URI uri = linkTo(methodOn(DeliveryOrderController.class).get(deliveryOrder.getId())).toUri();
        orderResultActions = mockMvc.perform(get(uri)
                .accept(MediaType.APPLICATION_JSON));
    }

    private void whenOrderRetrievedByLink() throws Exception {
            URI uri = linkTo(methodOn(DeliveryOrderController.class).getByOrderLink(deliveryOrder.getOrderLink())).toUri();
            orderResultActions = mockMvc.perform(get(uri)
                    .accept(MediaType.APPLICATION_JSON));
    }

    private void whenAllOrdersRetrieved() throws Exception {
        orderResultActions = mockMvc.perform(get(ordersUri)
                .accept(MediaType.APPLICATION_JSON));
    }

    private void givenDeliveryOrder() throws URISyntaxException {
        deliveryOrder = new DeliveryOrder();
        deliveryOrder.setOrderLink(new URI("http://localhost/orders/1"));
        deliveryOrder.setDeliveryOrderState(DeliveryOrderState.IN_PROGRESS);

        deliveryOrder =deliveryOrderRepository.save(deliveryOrder);
    }
}
