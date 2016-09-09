package epizza.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor=@__(@Autowired))
@Component
public class OrderResourceProcessor implements ResourceProcessor<Resource<Order>> {

    private final EntityLinks entityLinks;

    @Override
    public Resource<Order> process(Resource<Order> resource) {
        resource.add(entityLinks.linkToSingleResource(resource.getContent()));
        return resource;
    }

}
