package techno_express.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techno_express.backend.entity.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {
    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, String token) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("token", token);

        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, List<Product> products) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", products);

        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, Product product) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", product);

        return new ResponseEntity<>(body, status);
    }
}
