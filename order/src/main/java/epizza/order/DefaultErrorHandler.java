package epizza.order;

import org.springframework.context.MessageSource;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class DefaultErrorHandler extends RepositoryRestExceptionHandler {

    public DefaultErrorHandler(MessageSource messageSource) {
        super(messageSource);
    }
}
