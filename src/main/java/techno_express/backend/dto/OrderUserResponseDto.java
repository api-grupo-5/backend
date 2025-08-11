package techno_express.backend.dto;

import lombok.Data;
import techno_express.backend.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderUserResponseDto {
    private double amount;
    private LocalDateTime date;
    private List<OrderItemsUserResponseDto> order_items;
}
