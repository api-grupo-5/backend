package techno_express.backend.dto;

import lombok.Data;
import techno_express.backend.entity.OrderItem;

import java.util.List;
import java.util.Map;

@Data
public class OrderResponseDto {
    private Map<Long, List<OrderItem>> orders;

    // Constructor
    public OrderResponseDto() {
        this.orders = orders;
    }
}
