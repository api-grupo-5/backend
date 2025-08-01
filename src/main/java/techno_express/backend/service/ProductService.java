package techno_express.backend.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.ProductDto;
import techno_express.backend.entity.Product;
import techno_express.backend.entity.User;
import techno_express.backend.repository.ProductRepository;
import techno_express.backend.exception.ProductException;
import techno_express.backend.repository.UserRepository;
import techno_express.backend.util.ProductValidator;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductValidator productValidator;

    public List<ProductDto> getAllProducts(String request_id) {
        logger.info(request_id + " - obteniendo todos los productos...");
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductDto::new)
                .toList();
    }

    public ProductDto getProductById(String request_id, Long id) {
        logger.info(request_id + " - obteniendo producto con el id: '" + id + "'...");
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()){
            logger.error(request_id + " - el producto no existe");
            throw new ProductException.NotFound();
        }

        return new ProductDto(product.get());
    }

    public void createProduct(String request_id, ProductDto productDto) {
        User seller = productValidator.validateProductDto(request_id, productDto);

        logger.info(request_id + " - creando el producto: '" + productDto.getName() + "'...");
        Product newProduct = new Product();
        newProduct = productValidator.setProductData(newProduct, productDto, seller);

        logger.info(request_id + " - guardando producto...");
        productRepository.save(newProduct);
    }

    public void updateProduct(String request_id, ProductDto productDto) {
        User seller = productValidator.validateProductDto(request_id, productDto);

        logger.info(request_id + " - validando que exista el producto con el id: '" + productDto.getId() + "' en la base de datos...");
        Optional<Product> product = productRepository.findById(productDto.getId());

        if (product.isEmpty()){
            logger.error(request_id + " - el producto no existe");
            throw new ProductException.NotFound();
        }

        logger.info(request_id + " - actualizando el producto...");
        Product updatedProduct = productValidator.setProductData(product.get(), productDto, seller);

        logger.info(request_id + " - guardando cambios del producto...");
        productRepository.save(updatedProduct);
    }
}
