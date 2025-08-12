package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.context.RequestContext;
import techno_express.backend.dto.*;
import techno_express.backend.service.AuthService;
import techno_express.backend.util.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRegisterRequestDto authRegisterRequestDto,
                                      HttpServletRequest request) {
        String request_id = RequestContext.getRequestId();

        logger.info(request_id + " - inicio de register");
        logger.info(authRegisterRequestDto.toString());
        authService.register(request_id, authRegisterRequestDto);
        logger.info(request_id + " - fin de register");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthLoginRequestDto authLoginRequestDto,
                                   HttpServletRequest request) {
        String request_id = RequestContext.getRequestId();

        logger.info(request_id + " - inicio de login");
        AuthLoginResponseDto result = authService.login(request_id, authLoginRequestDto);
        logger.info(request_id + " - fin de login");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgot_password(@RequestBody AuthForgotPasswordRequestDto authForgotPasswordRequestDto,
                                                  HttpServletRequest request) {
        String request_id = RequestContext.getRequestId();

        logger.info(request_id + " - inicio de forgot_password");
        authService.forgot_password(request_id, authForgotPasswordRequestDto);
        logger.info(request_id + " - fin de forgot_password");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> reset_password(@RequestBody AuthResetPassowrdRequestDto authResetPassowrdRequestDto,
                                                 HttpServletRequest request) {
        String request_id = RequestContext.getRequestId();

        logger.info(request_id + " - inicio de reset_password");
        authService.reset_password(request_id, authResetPassowrdRequestDto);
        logger.info(request_id + " - fin de reset_password");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
