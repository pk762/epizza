package epizza.orderui;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderUiApplication.class)
@ActiveProfiles({"test","local"})
public class OrderUiApplicationTests {

    @Autowired
    private ApiProperties properties;

    @Test
    public void default_properties_are_set() {
        Assert.assertEquals(URI.create("http://localhost:8082/"), properties.getBaseUri());
    }
}
