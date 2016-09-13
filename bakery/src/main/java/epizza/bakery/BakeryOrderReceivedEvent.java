package epizza.bakery;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.time.LocalDateTime;

@Getter
@Setter
public class BakeryOrderReceivedEvent {

    private URI orderLink;

    private LocalDateTime estimatedTimeOfCompletion;
}
