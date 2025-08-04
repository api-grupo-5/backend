package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class CartException extends GenericException {
    public CartException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class NotFound extends CartException {
        public NotFound() {
            super(HttpStatus.CONFLICT, "0201", "El carrito no existe");
        }
    }

    public static class AlreadyExists extends CartException {
        public AlreadyExists() {
            super(HttpStatus.CONFLICT, "0411", "El usuario ya tiene un carrito creado");
        }
    }

    public static class InvalidData extends UserException {
        public InvalidData() {
            super(HttpStatus.BAD_REQUEST, "0412", "Datos del carrito inválidos");
        }
    }
}
