package epizza;

import org.junit.Test;

public class ConfigServerApplicationTests {

    @Test
    public void contextLoads() {
        ConfigServerApplication.main(new String[] { "--spring.profiles.active=native,test" });
    }
}
