package epizza.shared.event;

import static epizza.shared.event.EventPublisher.EVENT_PAYLOAD;
import static epizza.shared.event.EventPublisher.EVENT_TYPE;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

import org.springframework.messaging.handler.annotation.Payload;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractEventSubscriber {

    protected final ObjectMapper objectMapper;

    protected final String type;

    protected Map<String, Object> extractEvent(String jsonEvent) {
        try {
            return objectMapper.readValue(jsonEvent, new JsonMapTypeReference());
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not deserialize event '%s'", jsonEvent), e);
        }
    }

    @EPizzaEventListener
    public void consume(@Payload String jsonEvent) {
        Map<String, Object> event = extractEvent(jsonEvent);
        if (isOwnType(event)) {
            log.info("Consuming event '{}'", event);
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