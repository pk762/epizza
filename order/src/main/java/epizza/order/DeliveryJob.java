package epizza.order;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
@ToString
public class DeliveryJob {

    @NotNull
    private String deliveryBoy;

    @NotNull
    private LocalDateTime estimatedTimeOfDelivery;

}
