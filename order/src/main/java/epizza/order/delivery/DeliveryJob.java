package epizza.order.delivery;

import com.google.common.base.MoreObjects;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class DeliveryJob {
// SCHNIPP
    private final String deliveryBoy;

    private final LocalDateTime estimatedTimeOfDelivery;

    @JsonCreator
    @ConstructorProperties(value = {"deliveryBoy", "estimatedTimeOfDelivery"})
    public DeliveryJob(String deliveryBoy, LocalDateTime estimatedTimeOfDelivery) {
        this.deliveryBoy = deliveryBoy;
        this.estimatedTimeOfDelivery = estimatedTimeOfDelivery;
    }

    public String getDeliveryBoy() {
        return deliveryBoy;
    }

    public LocalDateTime getEstimatedTimeOfDelivery() {
        return estimatedTimeOfDelivery;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("deliveryBoy", deliveryBoy)
                .add("estimatedTimeOfDelivery", estimatedTimeOfDelivery)
                .toString();
    }
// SCHNAPP
}
