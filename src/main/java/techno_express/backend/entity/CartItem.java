package techno_express.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "cart_items")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    private LocalDateTime added_on;
    private LocalDateTime updated_on;

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", productId=" + (product != null ? product.getId() : "null") +
                ", productName=" + (product != null ? product.getName() : "null") +
                ", cartId=" + (cart != null ? cart.getId() : "null") +
                '}';
    }
}