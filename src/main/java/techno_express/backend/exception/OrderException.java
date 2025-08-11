package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class OrderException extends GenericException {
    public OrderException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class NotFound extends OrderException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND, "0201", "La orden no fue encontrada");
        }
    }

    public static class InvalidData extends UserException {
        public InvalidData() {
            super(HttpStatus.BAD_REQUEST, "0412", "Datos de la orden inválidos");
        }
    }
}
