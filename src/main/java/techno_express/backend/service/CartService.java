package techno_express.backend.service;

import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;

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
    public void save_cart(String request_id, CartDto cartDto) {
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
            logger.info(request_id + " - eliminando items anteriores del carrito del usuario");
            cartItemRepository.deleteByCartOwner(userOptional.get());
            cart = cartOptional.get();
            logger.info(request_id + " - agregando items del carrito en la db...");
        }

        for(CartItem item : cartDto.getItems()) {
            Optional<Product> product = productRepository.findById(item.getId());

            if(product.isPresent()) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setQuantity(item.getQuantity());
                cartItem.setProduct(product.get());

                cartItemRepository.save(cartItem);
            } else{
                logger.info(request_id + " - el producto '"+item.getId()+"' no existe");
            }
        }
    }

    @Transactional(readOnly = true)
    public HashMap<String, Object> load_cart(String request_id, CartDto cartDto) {
        logger.info(request_id + " - validando que exista el usuario id '"+ cartDto.getUser_id() +"'...");
        Optional<UserInformation> userOptional = userInformationRepository.findById(cartDto.getUser_id());

        if (userOptional.isEmpty()) {
            logger.error(request_id + "- el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - validando que el usuario tenga un carrito creado...");
        Optional<Cart> cartOptional = cartRepository.findByOwner(userOptional.get());

        if (cartOptional.isEmpty()) {
            logger.info(request_id + " - el usuario no tiene carrito");
            throw new CartException.NotExists();
        }

        logger.info(request_id + " - generando respuesta con el carrito del usuario...");
        List<CartItem> cartItems = cartItemRepository.findAllByCart_Id(cartOptional.get().getId());

        HashMap<String, Object> cart_data = new HashMap<>();
        List<Map<String, Object>> cartItemDataList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Optional<Product> productOpt = productRepository.findById(item.getProduct().getId());

            if (productOpt.isPresent()) {
                Product product = productOpt.get();

                Map<String, Object> itemData = new HashMap<>();
                itemData.put("id", product.getId());
                itemData.put("stock", product.getStock());
                itemData.put("title", product.getName());
                itemData.put("image", product.getImage());
                itemData.put("price", product.getPrice());
                itemData.put("quantity", item.getQuantity());

                cartItemDataList.add(itemData);
            } else {
                logger.info(request_id + " - El producto '" + item.getProduct().getId() + "' no existe");
            }
        }

        cart_data.put("cart_items", cartItemDataList);
        return cart_data;
    }

}
