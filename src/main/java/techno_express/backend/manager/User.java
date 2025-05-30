package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private LocalDateTime registered_on;
    private LocalDateTime last_logged_in;

    @OneToOne(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Role role;

    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<Ban> bans = new ArrayList<>();
}
