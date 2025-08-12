package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthLoginResponseDto {
    private Long user_id;
    private Long role_id;
    private Long cart_id;
    private String token;
}
