package commands


import java.util.stream.Collectors

import org.crsh.cli.Argument;
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.hateoas.PagedResources
import org.springframework.util.Assert

class prop {

    @Usage("Display a configuration property")
    @Command
    def main(InvocationContext context, @Argument String propertyName) {
        ConfigurableEnvironment env = context.getAttributes().get("spring.environment")
        Assert.notNull(env)
//        ConfigurableEnvironment env = beanFactory.getBean(ConfigurableEnvironment)
        return env.getProperty(propertyName)
    }

}