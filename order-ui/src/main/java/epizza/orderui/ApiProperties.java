package epizza.orderui;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="api")
public class ApiProperties {

    private URI baseUri = URI.create("http://localhost:8082/");

    public ApiProperties() {
    }

    public URI getBaseUri() {
        return baseUri;
    }
    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }
}
