package epizza.delivery.order;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import epizza.delivery.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order extends ResourceSupport {

    private Address deliveryAddress;

    private String comment;

    public Order withLink(Link link) {
        this.add(link);
        return this;
    }
}
