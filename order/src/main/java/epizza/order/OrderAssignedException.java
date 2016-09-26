package epizza.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Order already assigned.")
public class OrderAssignedException extends RuntimeException {

    public OrderAssignedException(String message) {
        super(message);
    }
}
