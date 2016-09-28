package epizza.delivery.order;

import org.springframework.hateoas.PagedResources;

public interface OrderServiceClient {

    void selectOrder(Integer orderId, DeliveryJob job);

    PagedResources<Order> getOrders();

}