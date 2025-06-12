package techno_express.backend.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private UserInformation seller;
}
