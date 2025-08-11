package techno_express.backend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techno_express.backend.context.RequestContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message,
                                                       HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);

        logger.info("----------- fin request " + requestId + " -> " + code + ": " + message + " -----------");
        return new ResponseEntity<>(body, status);
    }

    public static ResponseEntity<Object> buildResponse(HttpStatus status, String code, String message,
                                                       HttpServletRequest request, Object data) {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);
        body.put("data", data);

        logger.info("----------- fin request " + requestId + " -> " + code + ": " + message + " -----------");
        RequestContext.clear();
        return new ResponseEntity<>(body, status);
    }

    public static void writeResponse(HttpServletResponse response,
                                     HttpStatus status,
                                     String code,
                                     String message,
                                     HttpServletRequest request) throws IOException {
        Map<String, Object> body = new HashMap<>();
        String requestId = request.getHeader("request_id");

        body.put("request_id", requestId);
        body.put("code", code);
        body.put("message", message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        new ObjectMapper().writeValue(response.getWriter(), body);
        logger.info("----------- fin request " + requestId + " -> " + code + ": " + message + " -----------");
        RequestContext.clear();
    }
}
