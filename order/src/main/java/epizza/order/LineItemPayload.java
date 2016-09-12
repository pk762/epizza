package epizza.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class LineItemPayload {

    @NotNull
    private URI pizza;

    @NotNull
    @Min(1)
    private Integer amount;
}
