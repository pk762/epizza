package epizza.shared.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

@Slf4j
@AllArgsConstructor
public class EventPublisher {

    public static final String EVENT_TYPE = "type";

    public static final String EVENT_TIMESTAMP = "timestamp";

    public static final String EVENT_PAYLOAD = "payload";

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;

    public void publish(String type, String jsonPayload) {
        String event = createEvent(type, jsonPayload);
        log.info("Publishing event '{}'", event);
        rabbitTemplate.convertAndSend(event);
    }

    public void publish(String type, Map<String, Object> payloadMap) {
        String jsonPayload = null;
        try {
            jsonPayload = objectMapper.writeValueAsString(payloadMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(String.format("Could not serialize event from payload '%s'", payloadMap), e);
        }
        publish(type, jsonPayload);
    }

    protected String createEvent(String type, String jsonPayload) {
        try {
            return objectMapper.writeValueAsString(eventPayload(type, jsonPayload));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not serialize event from payload '%s'", jsonPayload), e);
        }
    }

    private Map<String, String> eventPayload(String type, String jsonPayload) throws IOException {
        return ImmutableMap.<String, String> builder()
                .put(EVENT_TYPE, type)
                .put(EVENT_TIMESTAMP, LocalDateTime.now().toString())
                .put(EVENT_PAYLOAD, objectMapper.readValue(jsonPayload, new JsonMapTypeReference()))
                .build();
    }
}
