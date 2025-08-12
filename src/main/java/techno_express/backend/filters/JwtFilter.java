package techno_express.backend.filters;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import techno_express.backend.context.RequestContext;
import techno_express.backend.service.JwtService;
import techno_express.backend.service.UserDetailsServiceImpl;
import techno_express.backend.util.ResponseBuilder;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        String request_id = request.getHeader("request_id");
        if(request_id == null || request_id.isEmpty()) {
            logger.error("Se envio un request sin request_id al emdpoint: " + path);
            ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0400", "No se procesara ninguna solicitud que no tenga request_id", request);
            return;
        }
        RequestContext.setRequestId(request_id);
        logger.info("-----------  inicio de request " + request_id + " ----------- ");

        // Allow OPTIONS requests (CORS preflight) to pass through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/auth/")) { //si es algo del login, proceda sin nada tramqui
            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/api/products")) { //si es algo de productos, proceda sin nada tramqui
            chain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        try{
            final String authHeader = request.getHeader("Authorization");

            if(authHeader == null || authHeader.isEmpty()){
                logger.error(request_id + " - no se envio ningun token");
                ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0400", "No se envio ningun token", request);
                return;
            }

            if(!authHeader.startsWith("Bearer ")) {
                logger.error(request_id + " - el token esta mal estructurado");
                ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0400", "Token mal estructurado", request);
                return;
            }

            String jwt = authHeader.substring(7);;
            String username = jwtService.extractUsername(jwt);

            if(username == null) {
                logger.error(request_id + " - el token es invalido");
                ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0400", "El token es invalido", request);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
                if (jwtService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.error(request_id + " - el token jwt es invalido o esta expirado");
                    ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0400", "Token inválido o expirado", request);
                    return;
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error(request_id + " - el token jwt expiro");
            ResponseBuilder.writeResponse(response, HttpStatus.UNAUTHORIZED, "0402", "Token expirado", request);
            return;
        } catch (io.jsonwebtoken.JwtException e) {
            logger.error(request_id + " - el token jwt es invalido");
            ResponseBuilder.writeResponse(response, HttpStatus.BAD_REQUEST, "0403", "Token JWT inválido", request);
            return;
        } catch (Exception e) {
            logger.error(request_id + " - error procesando el token JWT", e);
            ResponseBuilder.writeResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "0500", "Error interno", request);
            return;
        }

        chain.doFilter(request, response); // sin esto no sigue la cadena de ejecucion
    }
}
