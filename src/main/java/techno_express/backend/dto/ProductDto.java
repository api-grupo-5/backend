package techno_express.backend.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String image;
    private String category;
    private Long seller;
}