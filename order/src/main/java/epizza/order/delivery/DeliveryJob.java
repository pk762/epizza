package epizza.order.delivery;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class DeliveryJob {

    @NotNull
    private String deliveryBoy;

    @NotNull
    private LocalDateTime estimatedTimeOfDelivery;
}
