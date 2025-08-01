package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techno_express.backend.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
