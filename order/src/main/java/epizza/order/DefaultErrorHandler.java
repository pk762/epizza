package epizza.order;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class DefaultErrorHandler extends RepositoryRestExceptionHandler {

    private final Validator validator;

    private final MessageSourceAccessor messageSourceAccessor;

    public DefaultErrorHandler(MessageSource messageSource, Validator validator) {
        super(messageSource);
        this.validator = validator;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    @InitBinder
    public void registerValidator(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @ExceptionHandler
    public ResponseEntity<RepositoryConstraintViolationExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                new RepositoryConstraintViolationExceptionMessage(
                        new RepositoryConstraintViolationException(ex.getBindingResult()), messageSourceAccessor
                )
        );
    }
}
