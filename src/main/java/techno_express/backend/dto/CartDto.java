package techno_express.backend.dto;

import lombok.Data;
import techno_express.backend.entity.CartItem;

import java.util.ArrayList;

@Data
public class CartDto {
    private Long user_id;
    private ArrayList<CartItem> items;
}
