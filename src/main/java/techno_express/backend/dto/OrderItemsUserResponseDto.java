package techno_express.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemsUserResponseDto {
    private String category;
    private String description;
    private String name;
    private String image;
    private double price;
    private int quantity;
    private Long seller_id;
}
