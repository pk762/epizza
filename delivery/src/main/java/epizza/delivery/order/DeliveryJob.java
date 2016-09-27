package epizza.delivery.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DeliveryJob {

    private String deliveryBoy;

    private LocalDateTime estimatedTimeOfDelivery;

    public static DeliveryJob create(String deliveryBoy, Integer deliveryTimeInMinutes) {
        return new DeliveryJob(deliveryBoy, LocalDateTime.now().plusMinutes(deliveryTimeInMinutes)
                .truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
    }
}
