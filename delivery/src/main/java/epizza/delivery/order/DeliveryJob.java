package epizza.delivery.order;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryJob {

    private String deliveryBoy;

    private LocalDateTime estimatedTimeOfDelivery;
}
