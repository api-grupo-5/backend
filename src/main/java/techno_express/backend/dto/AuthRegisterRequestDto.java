package techno_express.backend.dto;

import lombok.Data;

@Data
public class AuthRegisterRequestDto {
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String phone;
    private String address;
    private int personal_id;
}
