package topjava.restaurantvoting.utils.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class IllegalRequestTimeException extends AppException {
    public IllegalRequestTimeException(String message) {
        super(HttpStatus.LOCKED, message, ErrorAttributeOptions.of(MESSAGE));
    }
}
