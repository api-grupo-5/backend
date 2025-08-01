package techno_express.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import techno_express.backend.dto.ProductDto;
import techno_express.backend.entity.Product;
import techno_express.backend.entity.User;
import techno_express.backend.exception.ProductException;
import techno_express.backend.exception.UserException;
import techno_express.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ProductValidator {
    private static final Logger logger = LoggerFactory.getLogger(ProductValidator.class);

    @Autowired
    private UserRepository userRepository;

    private static final List<String> VALID_CATEGORIES = List.of("perifericos", "computacion", "electrodomesticos");

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public User validate_product_dto(String request_id, ProductDto dto) {
        String name = dto.getName();
        String description = dto.getDescription();
        Double price = dto.getPrice();
        int stock = dto.getStock();
        String image = dto.getImage();
        String category = dto.getCategory();
        Long seller = dto.getSeller();

        logger.info(request_id + " - validando parámetros recibidos...");
        if (isBlank(name)) {
            logger.error(request_id + " - no enviaron nombre del producto");
            throw new ProductException.InvalidData();
        }

        if (isBlank(description)) {
            logger.error(request_id + " - no enviaron descripción del producto");
            throw new ProductException.InvalidData();
        }

        if (price == null || price < 1) {
            logger.error(request_id + " - el precio enviado ('" + price + "') es menor a 1");
            throw new ProductException.InvalidData();
        }

        if (stock <= 0) {
            logger.error(request_id + " - el stock enviado ('" + stock + "') es menor o igual a 0");
            throw new ProductException.InvalidData();
        }

        if (isBlank(image)) {
            logger.error(request_id + " - no enviaron el path de la imagen");
            throw new ProductException.InvalidData();
        }

        if (isBlank(category)) {
            logger.error(request_id + " - no enviaron la categoría del producto");
            throw new ProductException.InvalidData();
        }

        if (!VALID_CATEGORIES.contains(category.toLowerCase())) {
            logger.error(request_id + " - la categoría '" + category + "' es inválida");
            throw new ProductException.InvalidData();
        }

        if (seller == null || seller < 1) {
            logger.error(request_id + " - el id de usuario ('" + seller + "') no es válido");
            throw new ProductException.InvalidData();
        }

        logger.info(request_id + " - validando que exista algún usuario con el id: '" + seller + "'...");
        Optional<User> user = userRepository.findById(seller);
        if (user.isEmpty()) {
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        return user.get();
    }

    public Product setProductData(Product product, ProductDto dto, User seller) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImage(dto.getImage());
        product.setCategory(dto.getCategory());
        product.setSeller(seller);

        return product;
    }
}
