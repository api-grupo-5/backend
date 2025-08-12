package techno_express.backend.service;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.*;
import techno_express.backend.entity.*;
import techno_express.backend.exception.CartException;
import techno_express.backend.repository.*;
import techno_express.backend.util.CartValidator;

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
    private ProductRepository productRepository;

    @Autowired
    private CartValidator cartValidator;

    public HashMap<String, Object> load_cart(String request_id, CartDto cart_dto, Long cart_id) {
        Cart cart = cartValidator.validate_cart_dto(request_id, cart_dto, cart_id, false);

        logger.info(request_id + " - generando respuesta con el carrito del usuario...");
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        List<Map<String, Object>> cartItemDataList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Optional<Product> productOpt = productRepository.findById(item.getProduct().getId());

            if (productOpt.isPresent()) {
                Product product = productOpt.get();

                Map<String, Object> itemData = new HashMap<>();
                itemData.put("id", product.getId());
                itemData.put("stock", product.getStock());
                itemData.put("name", product.getName());
                itemData.put("image", product.getImage());
                itemData.put("price", product.getPrice());
                itemData.put("quantity", item.getQuantity());
                itemData.put("seller_id", product.getSeller().getId());

                cartItemDataList.add(itemData);
            } else {
                logger.info(request_id + " - El producto '" + item.getProduct().getId() + "' no existe. CartItem: "+ item.toString());
            }
        }

        HashMap<String, Object> cart_data = new HashMap<>();
        cart_data.put("cart_items", cartItemDataList);
        return cart_data;
    }

    @Transactional
    public void save_cart(String request_id, CartDto cart_dto, Long cart_id) {
        Cart cart = cartValidator.validate_cart_dto(request_id, cart_dto, cart_id, true);
        ArrayList<CartItem> cart_items = cart_dto.getItems();

        if(!Objects.equals(cart.getId(), cart_id)){
            cart_id = cart.getId();
        }

        if(cart_items == null){
            logger.error(request_id + " - no enviaron el campo 'items' con el carrito del usuario.");
            throw new CartException.InvalidData();
        }

        if(cart_items.isEmpty()) {
            logger.info(request_id + " - como no se envio ningun item en el request, se eliminara el carrito del usuario.");
            cartItemRepository.deleteByCartId(cart_id);
            return;
        }

        logger.info(request_id + " - validando si hay que eliminar articulos viejos...");
        List<Long> existingCartItems = cartItemRepository.findAllProductIdsByCartId(cart_id);

        if(!existingCartItems.isEmpty()) {
            List<Long> currentProductIds = cart_items.stream()
                    .map(CartItem::getId)
                    .filter(Objects::nonNull) // para evitar nulls si son nuevos
                    .toList();

            List<Long> validProductIds = productRepository.findAllById(currentProductIds)
                    .stream()
                    .map(Product::getId)
                    .toList();

            if (validProductIds.size() != currentProductIds.size()) {
                logger.error(request_id + " - hay articulos enviados que no existen en la base de datos");
                throw new CartException.InvalidData();
            }

            List<Long> itemsToRemove = existingCartItems.stream()
                    .filter(productId -> !currentProductIds.contains(productId))
                    .toList();

            logger.info(request_id + " - eliminando articulos viejos...");
            for (Long itemToRemove : itemsToRemove) {
                logger.info(itemToRemove.toString());
                cartItemRepository.deleteByCartIdAndProductId(cart_id, itemToRemove);
            }
        }

        logger.info(request_id + " - guardando y actualizando los articulos del carrito del usuario...");
        for(CartItem item : cart_items) {
            Optional<Product> product = productRepository.findById(item.getId());

            if (product.isPresent()) {
                Optional<CartItem> cart_item = cartItemRepository.findByCartIdAndProductId(cart_id, item.getId());
                CartItem checkout_item;

                if (cart_item.isPresent()) {
                    checkout_item = cart_item.get();
                    checkout_item.setUpdated_on(LocalDateTime.now());
                } else {
                    checkout_item = new CartItem();
                    checkout_item.setCart(cart);
                    checkout_item.setProduct(product.get());
                    checkout_item.setAdded_on(LocalDateTime.now());
                }

                checkout_item.setQuantity(item.getQuantity());
                cartItemRepository.save(checkout_item);
            } else {
                logger.info(request_id + " - el producto id '" + item.getId() + "' no existe. CartItem: " + item.toString());
                throw new CartException.InvalidData();
            }
        }
    }

    @Transactional
    public void delete_cart(String request_id, CartDto cart_dto, Long cart_id) {
        cartValidator.validate_cart_dto(request_id, cart_dto, cart_id, false);
        logger.info(request_id + " - eliminando el carrito perteneciente al usuario...");
        cartItemRepository.deleteByCartId(cart_id);
        cartRepository.deleteCartId(cart_id);
        logger.info(request_id + " - carrito eliminado correctamente");
    }
}
