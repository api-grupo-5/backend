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
import techno_express.backend.exception.UserException;
import techno_express.backend.exception.ProductException;
import techno_express.backend.repository.UserInformationRepository;
import techno_express.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllProducts(String request_id) {
        logger.info(request_id + " obteniendo todos los productos de la base de datos...");
        return productRepository.findAll();
    }

    public Product getProductById(String request_id, Long id) {
        logger.info(request_id + " obteniendo producto con el id: '" + id + "' de la base de datos...");
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()){
            logger.error(request_id + " - el producto no existe");
            throw new ProductException.NotFound();
        }

        return product.get();
    }

    public void saveProduct(String request_id, ProductDto productDto) {
        logger.info(request_id + " - validando que exista algun usuario con el id: '" + productDto.getSeller() + "' en la base de datos...");

        Optional<User> user = userRepository.findById(productDto.getSeller());

        if (user.isEmpty()){
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - creando el producto: '" + productDto.getName() + "'...");
        Product newProduct = new Product();
        newProduct.setName(productDto.getName());
        newProduct.setDescription(productDto.getDescription());
        newProduct.setPrice(productDto.getPrice());
        newProduct.setStock(productDto.getStock());
        newProduct.setImage(productDto.getImage());
        newProduct.setCategory(productDto.getCategory());
        newProduct.setSeller(user.get());

        productRepository.save(newProduct);
    }

    public void updateProduct(String request_id, ProductDto productDto) {
        logger.info(request_id + " - validando que exista el producto con el id: '" + productDto.getId() + "' en la base de datos...");
        Optional<Product> product = productRepository.findById(productDto.getId());

        if (product.isEmpty()){
            logger.error(request_id + " - el producto no existe");
            throw new ProductException.NotFound();
        }

        logger.info(request_id + " - validando que exista algun usuario con el id: '" + productDto.getSeller() + "' en la base de datos...");
        Optional<User> user = userRepository.findById(productDto.getSeller());
        if (user.isEmpty()){
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - actualizando el producto...");
        Product updatedProduct = product.get();
        updatedProduct.setName(productDto.getName());
        updatedProduct.setDescription(productDto.getDescription());
        updatedProduct.setPrice(productDto.getPrice());
        updatedProduct.setStock(productDto.getStock());
        updatedProduct.setImage(productDto.getImage());
        updatedProduct.setCategory(productDto.getCategory());
        updatedProduct.setSeller(user.get());

        productRepository.save(updatedProduct);
    }
}
