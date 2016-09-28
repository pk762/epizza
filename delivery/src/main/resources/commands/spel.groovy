package commands

import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory
import org.springframework.context.expression.BeanFactoryResolver
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert

class spel {

    @Usage("Executes a SpEL expression")
    @Command
    def main(InvocationContext context, @Argument String spel) {
        BeanFactory beanFactory = context.getAttributes().get("spring.beanfactory")
        Assert.notNull(beanFactory)

        StandardEvaluationContext eval = new StandardEvaluationContext();
        eval.setBeanResolver(new BeanFactoryResolver(beanFactory))
        SpelExpressionParser parser = new SpelExpressionParser()
        return parser.parseExpression(spel).getValue(eval)
    }

}
