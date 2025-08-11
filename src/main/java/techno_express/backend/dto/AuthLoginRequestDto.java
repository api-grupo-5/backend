package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthLoginRequestDto {
    private String email;
    private String password;
}
