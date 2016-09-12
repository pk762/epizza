package epizza.shared.event;

import static epizza.shared.event.EventPublisher.EVENT_PAYLOAD;
import static epizza.shared.event.EventPublisher.EVENT_TYPE;

import java.io.IOException;
import java.util.Map;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractEventSubscriber {

    protected final ObjectMapper objectMapper;

    protected final String type;

    protected AbstractEventSubscriber(ObjectMapper objectMapper, String type) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null.");
        Assert.notNull(type, "Type must not be null.");

        this.objectMapper = objectMapper;
        this.type = type;
    }

    protected Map<String, Object> extractEvent(String jsonEvent) {
        try {
            return objectMapper.readValue(jsonEvent, new JsonMapTypeReference());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not deserialize event '%s'", jsonEvent), e);
        }
    }

    @EPizzaEventListener
    public void consume(@Payload String jsonEvent) {
        final Map<String, Object> event = extractEvent(jsonEvent);
        if (isOwnType(event)) {
            handleOwnType(event);
        } else {
            handleForeignType(event);
        }
    }

    protected String getType(Map<String, Object> event) {
        return event.get(EVENT_TYPE).toString();
    }

    protected boolean isOwnType(Map<String, Object> event) {
        return type.equals(getType(event));
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> getPayload(Map<String, Object> event) {
        return (Map<String, Object>) event.get(EVENT_PAYLOAD);
    }

    protected void handleForeignType(Map<String, Object> event) {
        // overwrite me if needed
    }

    protected abstract void handleOwnType(Map<String, Object> event);
}