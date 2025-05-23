package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "cart_item")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cartItemNumber = 0; //funciona como un id unico de los productos en el carrito
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}