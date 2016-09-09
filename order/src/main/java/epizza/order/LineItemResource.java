package epizza.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;

import javax.money.MonetaryAmount;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@NoArgsConstructor
public class LineItemResource extends ResourceSupport {

    @NotNull
    private URI pizza;

    @NotNull
    @Min(1)
    private Integer amount;

    private MonetaryAmount price;
}
