package epizza.order;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderResourceProcessor implements ResourceProcessor<Resource<Order>>  {

    @Override
    public Resource<Order> process(Resource<Order> resource) {
        Link link = linkTo(DeliveryController.class, resource.getContent().getId()).withRel("delivery");
        resource.add(link);
        return resource;
    }
}
