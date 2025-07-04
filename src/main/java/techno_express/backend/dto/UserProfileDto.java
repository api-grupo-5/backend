package techno_express.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
