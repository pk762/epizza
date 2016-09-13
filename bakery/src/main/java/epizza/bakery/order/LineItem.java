package epizza.bakery.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URI;

@Getter
@Setter
@ToString(of = { "pizza", "amount" })
public class LineItem {

    private URI pizza;

    private Integer amount;
}
