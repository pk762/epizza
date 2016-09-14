package epizza.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "don't cheat!")
public class OrderAssignedException extends RuntimeException {
}
