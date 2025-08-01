package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthResetPasswordDto {
    private String token;
    private String password;
}
