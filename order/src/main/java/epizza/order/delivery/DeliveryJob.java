package epizza.order.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;

@Getter
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
@ToString
public class DeliveryJob {

    @NotNull
    private String deliveryBoy;

    @NotNull
    private LocalDateTime estimatedTimeOfDelivery;

}
