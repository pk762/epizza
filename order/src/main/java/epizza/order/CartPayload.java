package epizza.order;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.google.common.collect.Lists;

@Data
public class CartPayload {

    @Valid
    private List<LineItemPayload> lineItems = Lists.newArrayList();

    private String comment;

    @Valid
    @NotNull
    private Address deliveryAddress;
}
