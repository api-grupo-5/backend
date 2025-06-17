package techno_express.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity(name="users_information")
public class UserInformation {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    private String first_name;
    private String last_name;
    private int personal_id;
    private String email;
    private String phone;
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders; // historial de pedidos

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart current_cart_id;
}
