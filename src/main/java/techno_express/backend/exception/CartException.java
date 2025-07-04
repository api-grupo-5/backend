package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class CartException extends GenericException {
    public CartException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class AlreadyExists extends CartException {
        public AlreadyExists() {
            super(HttpStatus.CONFLICT, "0411", "El usuario ya tiene un carrito creado");
        }
    }
}
