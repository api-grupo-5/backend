package techno_express.backend.util;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techno_express.backend.entity.Product;
import techno_express.backend.service.AuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);

        logger.info("----------- fin request "+requestId+" -> "+code+": "+message+" -----------");
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, HashMap<String, Object> result) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", result);

        logger.info("----------- fin request "+requestId+" -> "+code+": "+message+" -----------");
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, List<Product> products) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", products);

        logger.info("----------- fin request "+requestId+" -> "+code+": "+message+" -----------");
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message, HttpServletRequest request, Product product) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", product);

        logger.info("----------- fin request "+requestId+" -> "+code+": "+message+" -----------");
        return new ResponseEntity<>(body, status);
    }
}
