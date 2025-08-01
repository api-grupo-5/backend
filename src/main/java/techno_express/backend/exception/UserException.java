package techno_express.backend.exception;

import org.springframework.http.HttpStatus;

public abstract class UserException extends GenericException {
    public UserException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }

    // Subclases internas
    public static class NotFound extends UserException {
        public NotFound() {
            super(HttpStatus.NOT_FOUND, "0201", "El usuario no fue encontrado");
        }
    }

    public static class ExpiredToken extends UserException {
        public ExpiredToken() {
            super(HttpStatus.BAD_REQUEST, "0403", "Token vencido");
        }
    }

    public static class InvalidOtpToken extends UserException {
        public InvalidOtpToken() {
            super(HttpStatus.BAD_REQUEST, "0404", "Token de recuperacion de cuenta inexistente");
        }
    }

    public static class AlreadyExists extends UserException {
        public AlreadyExists() {
            super(HttpStatus.CONFLICT, "0410", "El usuario ya existe");
        }
    }

    public static class InvalidData extends UserException {
        public InvalidData() {
            super(HttpStatus.BAD_REQUEST, "0412", "Datos del usuario inválidos");
        }
    }
}
