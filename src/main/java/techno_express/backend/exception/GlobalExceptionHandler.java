package techno_express.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import techno_express.backend.util.ResponseBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(UserException ex, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(ex.getStatus(), ex.getCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "0400", "Violación de integridad de datos", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "0500","Ocurrió un error inesperado", request);
    }
}
