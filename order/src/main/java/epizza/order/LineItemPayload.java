package epizza.order;

import lombok.Data;

import java.net.URI;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class LineItemPayload {

    @NotNull
    private URI pizza;

    @NotNull
    @Min(1)
    private Integer quantity;
}
