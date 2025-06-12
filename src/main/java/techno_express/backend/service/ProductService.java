package techno_express.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techno_express.backend.entity.Product;
import techno_express.backend.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Producto no encontrado con el id:" + id)
        );
    }

    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        if (this.productRepository.existsById(product.getId())) {
            return this.productRepository.save(product);
        }
        return null;
    }

    public void deleteProduct(Long id) {
    }
}
