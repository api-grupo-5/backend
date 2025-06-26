package techno_express.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private LocalDateTime registered_on;
    private LocalDateTime last_logged_in;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;
}
