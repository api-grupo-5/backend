package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class OrderException extends GenericException {
    public OrderException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class NotExists extends OrderException {
        public NotExists() {
            super(HttpStatus.CONFLICT, "0201", "La orden no existe");
        }
    }

    public static class InvalidData extends UserException {
        public InvalidData() {
            super(HttpStatus.BAD_REQUEST, "0412", "Datos de la orden inválidos");
        }
    }
}
