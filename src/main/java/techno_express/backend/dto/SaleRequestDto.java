package techno_express.backend.dto;

import lombok.Data;

@Data
public class SaleRequestDto {
    private Long productId;
    private Integer quantity;
    private String buyer;
}
