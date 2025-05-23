package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techno_express.backend.manager.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> id(Long id);
}
