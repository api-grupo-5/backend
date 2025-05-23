package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="usersInformation")
public class UserInformation {
    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name="user_id")
    private User user;

    private String first_name;
    private String full_name;
    private int personal_id;
    private String email;
    private String phone;
    private String address;
}
