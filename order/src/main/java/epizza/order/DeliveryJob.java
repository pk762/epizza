package epizza.order;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DeliveryJob {

    private String deliveryBoy;

    private LocalDateTime estimatedTimeOfDelivery;


}
