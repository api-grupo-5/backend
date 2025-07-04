package techno_express.backend.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.*;
import techno_express.backend.entity.*;
import techno_express.backend.exception.CartException;
import techno_express.backend.exception.UserException;
import techno_express.backend.repository.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void create_cart(String request_id, CartDto cartDto) {
        logger.info(request_id + " - validando que exista el usuario id '"+ cartDto.getUser_id() +"'...");
        Optional<UserInformation> userOptional = userInformationRepository.findById(cartDto.getUser_id());

        if (userOptional.isEmpty()) {
            logger.error(request_id + "- el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - validando que el usuario no tenga un carrito creado...");
        Optional<Cart> cartOptional = cartRepository.findByOwner(userOptional.get());

        Cart cart;
        if (cartOptional.isEmpty()) {
            logger.info(request_id + " - creando carrito para el usuario...");
            cart = new Cart();
            cart.setCreated_on(LocalDateTime.now());
            cart.setOwner(userOptional.get());
            cartRepository.save(cart);
            logger.info(request_id + " - creando items del carrito en la db...");
        } else {
            cart = cartOptional.get();
            logger.info(request_id + " - agregando items del carrito en la db...");
        }

        for(CartItem item : cartDto.getItems()) {
            Optional<Product> product = productRepository.findById(item.getId());

            if(product.isPresent()) {
                Optional<CartItem> dupped_item = cartItemRepository.findByCartAndProduct(cart, product.get());
                CartItem cartItem;

                if(dupped_item.isPresent()) {
                    cartItem = dupped_item.get();
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                } else{
                    cartItem = new CartItem();
                    cartItem.setCart(cart);
                    cartItem.setQuantity(item.getQuantity());
                    cartItem.setProduct(product.get());
                }

                cartItemRepository.save(cartItem);
            }
        }
    }
}
