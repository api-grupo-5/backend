package techno_express.backend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // Allow OPTIONS requests (CORS preflight) without request_id
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestId = request.getHeader("request_id");
        if (requestId == null || requestId.isEmpty()) {
            System.out.println("Se envió una peticion sin request_id: " + request.getRequestURI());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing request_id header");
            return false;
        }

        request.setAttribute("request_id", requestId);
        return true;
    }
}
