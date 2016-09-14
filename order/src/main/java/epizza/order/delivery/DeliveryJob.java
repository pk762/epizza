package epizza.order.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;

@Getter
@ToString
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
public class DeliveryJob {

    @NotNull
    private String deliveryBoy;

    @NotNull
    private LocalDateTime estimatedTimeOfDelivery;
}
