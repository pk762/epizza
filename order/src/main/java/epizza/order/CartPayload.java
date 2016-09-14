package epizza.order;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

@Data
public class CartPayload {

    @Valid
    private List<LineItemPayload> lineItems = Lists.newArrayList();

    private String comment;

    @Length(min = 1, max = 12)
    @AllowedChars
    private String couponCode;

    @Valid
    @NotNull
    private Address deliveryAddress;
}
