package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String registered_on;
    private String last_logged_in;

    @JoinColumn(name="cart_id")
    private Cart cart;
}
