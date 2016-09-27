package epizza.order.checkout;

import com.google.common.collect.Lists;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CartPayload {

    @Valid
    private List<LineItemPayload> lineItems = Lists.newArrayList();

    private String comment;

    @Valid
    @NotNull
    private Address deliveryAddress;
}
