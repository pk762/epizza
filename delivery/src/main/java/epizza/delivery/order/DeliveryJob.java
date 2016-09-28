package epizza.delivery.order;

import static java.time.temporal.ChronoUnit.MINUTES;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DeliveryJob {

    String deliveryBoy;

    LocalDateTime estimatedTimeOfDelivery;

    public static DeliveryJob create(String deliveryBoy, Integer deliveryTimeInMinutes) {
        return new DeliveryJob(deliveryBoy, LocalDateTime.now().plusMinutes(deliveryTimeInMinutes)
                .truncatedTo(MINUTES));
    }
}
