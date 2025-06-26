package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.AuthRequestDto;
import techno_express.backend.dto.UserRegisterDto;
import techno_express.backend.service.AuthService;
import techno_express.backend.util.ResponseBuilder;
import techno_express.backend.dto.AuthResponseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

        logger.info("Login request received with request_id: " + request_id);
        logger.info(request_id + " - inicio de login");
        String token = authService.login(request_id, authRequestDto);
        logger.info(request_id + " - fin de login");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, token);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestAttribute("request_id") String request_id, @RequestParam String email) {
        authService.sendRecoveryToken(email);
        return ResponseEntity.ok("Se generó un token de recuperación (revisá logs para verlo)");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
