package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class ProductException extends GenericException {
    public ProductException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class NotFound extends ProductException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND, "0202", "El producto no fue encontrado");
        }
    }
}
