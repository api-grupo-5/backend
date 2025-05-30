package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String permissions;
}
