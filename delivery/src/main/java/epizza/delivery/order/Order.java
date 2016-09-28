package epizza.delivery.order;

import lombok.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
public class Order {

    @JsonProperty("_id")
    String orderId;

    Address deliveryAddress;

    String comment;

}
