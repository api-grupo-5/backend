package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "user")
    private List<Cart> carts; // Historial de carritos

    @OneToOne
    @JoinColumn(name = "current_cart_id")
    private Cart actual_cart;
}
