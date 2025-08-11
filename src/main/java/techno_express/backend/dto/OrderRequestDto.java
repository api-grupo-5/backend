package techno_express.backend.dto;

import lombok.Data;
import techno_express.backend.entity.OrderItem;
import java.util.List;

@Data
public class OrderRequestDto {
    private List<OrderItem> items;
    private double amount;
    private Long user_id;
    private Long cart_id;
}
