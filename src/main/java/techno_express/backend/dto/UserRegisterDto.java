package techno_express.backend.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String address;
    private int personal_id;
    private String role;
}
