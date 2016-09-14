package epizza.order;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
public class OrderResourceProcessor implements ResourceProcessor<Resource<Order>>  {

    @Override
    public Resource<Order> process(Resource<Order> resource) {

        return resource;
    }
}
