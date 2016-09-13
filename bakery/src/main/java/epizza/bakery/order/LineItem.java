package epizza.bakery.order;

import lombok.Data;

import java.net.URI;

@Data
public class LineItem {

    private URI pizza;

    private Integer amount;
}
