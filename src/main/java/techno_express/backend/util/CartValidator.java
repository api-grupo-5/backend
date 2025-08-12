package techno_express.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import techno_express.backend.dto.CartDto;
import techno_express.backend.entity.Cart;
import techno_express.backend.entity.UserInformation;
import techno_express.backend.exception.CartException;
import techno_express.backend.exception.UserException;
import techno_express.backend.repository.CartRepository;
import techno_express.backend.repository.UserInformationRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CartValidator {
    private static final Logger logger = LoggerFactory.getLogger(CartValidator.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public Cart validate_cart_dto(String request_id, CartDto dto, Long cart_id, boolean create) {
        Long user_id = dto.getUser_id();

        logger.info(request_id + " - validando interfaces recibidas...");
        if (isBlank(String.valueOf(user_id))) {
            logger.error(request_id + " - no enviaron id de usuario");
            throw new CartException.InvalidData();
        }

        if (user_id < 1) {
            logger.error(request_id + " - el id de usuario ('" + user_id + "') no es válido");
            throw new CartException.InvalidData();
        }

        logger.info(request_id + " - validando que exista el usuario id: '{}'...", dto.getUser_id());
        Optional<UserInformation> userOptional = userInformationRepository.findById(dto.getUser_id());

        if (userOptional.isEmpty()) {
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - validando que exista el carrito id: '{}'...", cart_id);
        Optional<Cart> cartOptional = cartRepository.findById(cart_id);
        if(!create){
            if(cartOptional.isEmpty()) {
                logger.error(request_id + " - el carrito no existe");
                throw new CartException.NotFound();
            } else{
                logger.info(request_id + " - validando que el id del dueño del carrito coincida con el del usuario...");
                if (!cartOptional.get().getOwner().getId().equals(dto.getUser_id())) {
                    logger.error(request_id + " - el carrito no pertenece al usuario");
                    throw new CartException.InvalidData();
                }
            }
        } else{
            logger.info(request_id + " - se solicito validar si hay que crearle un carrito al usuario...");
            Optional<Cart> cart = cartRepository.findByOwner(userOptional.get());
            Cart output_cart;

            if (cart.isEmpty()) {
                logger.info(request_id + " - el usuario no tiene carrito, asi que se le creara uno...");
                output_cart = new Cart();
                output_cart.setCreated_on(LocalDateTime.now());
                output_cart.setOwner(userOptional.get());
                logger.info(request_id + " - guardando carrito...");
                cartRepository.save(output_cart);
            } else{
                logger.info(request_id + " - el usuario ya tiene carrito actualmente...");
                logger.info(request_id + " - validando que el id del dueño del carrito coincida con el del usuario...");
                if (!cartOptional.get().getOwner().getId().equals(dto.getUser_id())) {
                    logger.error(request_id + " - el carrito no pertenece al usuario");
                    throw new CartException.InvalidData();
                }

                output_cart = cart.get();
            }

            return output_cart;
        }

        return cartOptional.get();
    }
}
