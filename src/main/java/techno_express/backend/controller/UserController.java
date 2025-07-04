package techno_express.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import techno_express.backend.dto.UserProfileDto;
import techno_express.backend.dto.UserProfileUpdateDto;
import techno_express.backend.service.UserService;
import techno_express.backend.util.ResponseBuilder;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserProfileDto profile = userService.getProfileByUsername(username);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestAttribute("request_id") String requestId,
                                           @RequestBody UserProfileUpdateDto updatedInfo,
                                           HttpServletRequest request) {
        userService.updateCurrentUserProfile(updatedInfo);
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "Datos actualizados correctamente", request);
    }
}
