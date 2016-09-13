package commands


import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory;

class hello {

    @Usage("Say Hello")
    @Command
    def main(InvocationContext context) {
        BeanFactory beanFactory = context.getProperty("spring.beanfactory")
        return "Hello " + beanFactory
    }

}