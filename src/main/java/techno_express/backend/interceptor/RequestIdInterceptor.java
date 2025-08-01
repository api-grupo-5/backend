package techno_express.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import techno_express.backend.controller.AuthController;
import techno_express.backend.util.ResponseBuilder;

import java.io.IOException;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RequestIdInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // Allow OPTIONS requests (CORS preflight) without request_id
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestId = request.getHeader("request_id");
        if (requestId == null || requestId.isEmpty()) {
            logger.error("Se envió una peticion sin request_id al endpoint: " + request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Missing request_id header\"}");
            response.getWriter().flush();
            return false;
        }

        logger.info("----------- inicio request "+requestId+" -----------");
        request.setAttribute("request_id", requestId);
        return true;
    }
}
