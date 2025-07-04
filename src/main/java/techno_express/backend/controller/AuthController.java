package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.*;
import techno_express.backend.service.AuthService;
import techno_express.backend.util.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestAttribute("request_id") String request_id,
                                      @RequestBody UserRegisterDto userRegisterDto,
                                      HttpServletRequest request) {

        logger.info(request_id + " - inicio de register");
        authService.register(request_id, userRegisterDto);
        logger.info(request_id + " - fin de register");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestAttribute("request_id") String request_id,
                                   @RequestBody AuthRequestDto authRequestDto,
                                   HttpServletRequest request) {

        logger.info(request_id + " - inicio de login");
        HashMap<String, Object> result = authService.login(request_id, authRequestDto);
        logger.info(request_id + " - fin de login");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestAttribute("request_id") String request_id,
                                                 @RequestBody AuthForgotPasswordDto authForgotPasswordDto,
                                                 HttpServletRequest request) {
        logger.info(request_id + " - inicio de forgot-password");
        authService.sendRecoveryToken(request_id, authForgotPasswordDto);
        logger.info(request_id + " - fin de forgot-password");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(
            @RequestAttribute("request_id") String request_id,
            @RequestBody AuthResetPasswordDto authResetPasswordDto,
            HttpServletRequest request) {

        logger.info(request_id + " - inicio de reset-password");
        authService.resetPassword(request_id, authResetPasswordDto);
        logger.info(request_id + " - fin de reset-password");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
