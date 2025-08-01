package techno_express.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import techno_express.backend.util.ResponseBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CartException.class)
    public ResponseEntity<Object> handleCartException(CartException ex, HttpServletRequest request) {
        logger.error("Cart Exception: ", ex);
        return ResponseBuilder.buildResponse(ex.getStatus(), ex.getCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<Object> handleProductException(ProductException ex, HttpServletRequest request) {
        logger.error("Product Exception: ", ex);
        return ResponseBuilder.buildResponse(ex.getStatus(), ex.getCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(UserException ex, HttpServletRequest request) {
        logger.error("User Exception: ", ex);
        return ResponseBuilder.buildResponse(ex.getStatus(), ex.getCode(), ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(Exception ex, HttpServletRequest request) {
        logger.error("Data Integrity Violation: ", ex);
        return ResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST, "0400", "Violación de integridad de datos", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error: ", ex);
        return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "0500","Ocurrió un error inesperado", request);
    }
}
