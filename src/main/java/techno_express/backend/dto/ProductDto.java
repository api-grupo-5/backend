package techno_express.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techno_express.backend.entity.Product;

@Data
@NoArgsConstructor
public class ProductDto {
    private static final Logger logger = LoggerFactory.getLogger(ProductDto.class);

    private Long id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String image;
    private String category;
    private Long seller;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.image = product.getImage();
        this.category = product.getCategory();
        this.seller = product.getSeller() != null ? product.getSeller().getId() : null;

        logger.info("ProductDto creado: id = {}, name = {}, description = {}, price = {}, stock = {}, image = {}, category = {}, seller = {}",
                this.id, this.name, this.description, this.price, this.stock, this.image, this.category, this.seller);
    }
}