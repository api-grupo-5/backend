package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techno_express.backend.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
