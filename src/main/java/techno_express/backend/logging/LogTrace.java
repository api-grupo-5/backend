package techno_express.backend.logging;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogTrace implements Filter {
    private static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String request_id = RequestIDGenerator.generateRequestID(); // creo el request_id
        MDC.put(REQUEST_ID, request_id); // lo agrego a la peticion

        try {
            chain.doFilter(request, response); // sigue la ejecucion comun
        } finally {
            MDC.remove(REQUEST_ID); // lo borro para evitar que exista la posibilidad de que se reutilice el request_id
        }
    }
}