package epizza.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface OrderMixin {
    @JsonProperty("_id")
    Long getId();
}
