package epizza.delivery.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

    @JsonProperty("_id")
    private String orderId;

    private Address deliveryAddress;

    private String comment;

}
