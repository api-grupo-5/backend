package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthResetPassowrdRequestDto {
    private String password;
    private String token;
}
