package epizza.order.checkout;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import epizza.order.delivery.DeliveryController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public class OrderResourceProcessor implements ResourceProcessor<Resource<Order>> {

    @Override
    public Resource<Order> process(Resource<Order> resource) {
        Link link = linkTo(DeliveryController.class, resource.getContent().getId()).withRel("delivery");
        resource.add(link);
        return resource;
    }
}
