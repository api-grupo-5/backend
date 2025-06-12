package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;
}
