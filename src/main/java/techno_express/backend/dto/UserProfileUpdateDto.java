package techno_express.backend.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
