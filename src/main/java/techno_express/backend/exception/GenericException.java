package techno_express.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class GenericException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String response_description;

    public GenericException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
        this.response_description = message;
    }
}
