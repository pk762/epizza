package epizza.bakery;

import lombok.Data;

import java.net.URI;
import java.time.LocalDateTime;

@Data
public class BakeryOrderReceivedEvent {

    private URI orderLink;

    private LocalDateTime estimatedTimeOfCompletion;
}
