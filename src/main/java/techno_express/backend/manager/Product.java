package techno_express.backend.manager;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String image;
    private String category;

    @ManyToOne(fetch = FetchType.EAGER) //eager = trae la info siempre de una
    @JoinColumn(name = "seller_id", nullable = false) // nullable = sin vendedor, no se crea
    private User seller;
}
