package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techno_express.backend.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}