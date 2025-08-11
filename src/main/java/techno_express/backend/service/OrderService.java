package techno_express.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techno_express.backend.dto.OrderItemsUserResponseDto;
import techno_express.backend.dto.OrderRequestDto;
import techno_express.backend.dto.OrderUserResponseDto;
import techno_express.backend.entity.*;
import techno_express.backend.exception.CartException;
import techno_express.backend.exception.OrderException;
import techno_express.backend.exception.ProductException;
import techno_express.backend.exception.UserException;
import techno_express.backend.repository.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    private static final List<String> VALID_CATEGORIES = List.of("perifericos", "computacion", "electrodomesticos");

    @Transactional
    public void create_order(String request_id, OrderRequestDto orderRequestDto) {
        logger.info(request_id + " - validando interfaces recibidas...");
        Long user_id = orderRequestDto.getUser_id();
        double price = orderRequestDto.getAmount();
        Long cart_id = orderRequestDto.getCart_id();
        List<OrderItem> items = orderRequestDto.getItems();

        if(user_id < 1){
            logger.error(request_id + " - el usuario es invalido, enviaron el id '{}'", user_id);
            throw new UserException.InvalidData();
        }

        if(price < 1){
            logger.error(request_id + " - el monto es invalido, enviaron '{}'", price);
            throw new OrderException.InvalidData();
        }

        if(cart_id < 1){
            logger.error(request_id + " - el carrito es invalido, enviaron el id '{}'", cart_id);
            throw new CartException.InvalidData();
        }

        if(items.isEmpty()){
            logger.error(request_id + " - la lista de articulos esta vacia");
            throw new OrderException.InvalidData();
        }

        double total_items_price = items.stream().mapToDouble(OrderItem::getPrice).sum();
        if(price != total_items_price){
            logger.error(request_id + " - el costo total enviado no coincide con la suma de los precios de los articulos ('{}' vs '{}')", price, total_items_price);
            throw new CartException.InvalidData();
        }

        logger.info(request_id + " - validando que exista el carrito enviado...");
        Optional<Cart> cart = cartRepository.findById(orderRequestDto.getCart_id());

        if (cart.isEmpty()) {
            logger.error(request_id + " - el carrito no existe");
            throw new CartException.NotFound();
        }

        logger.info(request_id + " - validando que exista el usuario enviado...");
        Optional<User> user = userRepository.findById(user_id);

        if (user.isEmpty()) {
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - validando que el carrito enviado pertenezca al usuario enviado...");
        Cart using_cart = cart.get();
        if(!using_cart.getOwner().getId().equals(user_id)){
            logger.error(request_id + " - el carrito id '{}' no pertenece al usuario id '{}'", using_cart.getId(), user_id);
            throw new CartException.InvalidData();
        }

        logger.info(request_id + " - creando orden...");
        Order order = new Order();
        order.setAmount(price);
        order.setDate(LocalDateTime.now());
        order.setCustomer(using_cart.getOwner());
        orderRepository.save(order);

        logger.info(request_id + " - guardando articulos de la orden...");
        for (OrderItem item : orderRequestDto.getItems()) {
            String item_category = item.getCategory();
            String item_name = item.getName();
            String item_description = item.getDescription();
            double item_price = item.getPrice();
            int item_quantity = item.getQuantity();
            User item_seller = item.getSeller();
            Long item_seller_id =  item_seller.getId();
            Product item_product = item.getProduct();
            Long item_product_id =  item_product.getId();

            if(isBlank(item_category)){
                logger.error(request_id + " - la categoria enviada esta vacia");
                throw new OrderException.InvalidData();
            }

            if (!VALID_CATEGORIES.contains(item_category.toLowerCase())) {
                logger.error(request_id + " - la categoría '" + item_category + "' es inválida");
                throw new OrderException.InvalidData();
            }

            if(isBlank(item_name)){
                logger.error(request_id + " - el nombre enviado esta vacio");
                throw new OrderException.InvalidData();
            }

            if(isBlank(item_description)){
                logger.error(request_id + " - la descripcion enviada esta vacia");
                throw new OrderException.InvalidData();
            }

            if(item_price < 1){
                logger.error(request_id + " - el precio enviado es menor a 1 ('{}')", item_price);
                throw new OrderException.InvalidData();
            }

            if(item_quantity < 1){
                logger.error(request_id + " - la cantidad enviada es menor a 1 ('{}')", item_quantity);
                throw new OrderException.InvalidData();
            }

            if(item_seller_id < 1){
                logger.error(request_id + " - el id de usuario enviado es menor a 1 ('{}')", item_seller_id);
                throw new UserException.InvalidData();
            }

            Optional<User> seller = userRepository.findById(item_seller_id);
            if (seller.isEmpty()) {
                logger.error(request_id + " - el usuario id '{}' no existe", item_seller_id);
                throw new UserException.NotFound();
            }

            if(item_product_id < 1){
                logger.error(request_id + " - el id de producto enviado es menor a 1 ('{}')", item_product_id);
                throw new OrderException.InvalidData();
            }

            Optional<Product> product = productRepository.findById(item_product_id);
            if (product.isEmpty()) {
                logger.error(request_id + " - el producto id '{}' no existe", item_product_id);
                throw new ProductException.NotFound();
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setCategory(item.getCategory());
            orderItem.setDescription(item.getDescription());
            orderItem.setImage(item.getImage());
            orderItem.setName(item.getName());
            orderItem.setPrice(item.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setSeller(item.getSeller());
            orderItemRepository.save(orderItem);
        }
    }

    public Map<Long, OrderUserResponseDto> get_order_by_user_id(String request_id, OrderRequestDto orderRequestDto) {
        logger.info(request_id + " - validando interfaces recibidas...");
        Long user_id = orderRequestDto.getUser_id();

        if(user_id < 1){
            logger.error(request_id + " - el usuario es invalido, enviaron el id '{}'", user_id);
            throw new UserException.InvalidData();
        }

        logger.info(request_id + " - validando que exista el usuario enviado...");
        Optional<User> user = userRepository.findById(user_id);

        if (user.isEmpty()) {
            logger.error(request_id + " - el usuario no existe");
            throw new UserException.NotFound();
        }

        logger.info(request_id + " - validando si el usuario alguna orden...");
        List<Order> orders = orderRepository.findAllByCustomerId(user_id);

        if (orders.isEmpty()) {
            logger.error(request_id + " - el usuario no tiene ninguna orden");
            throw new OrderException.NotFound();
        }

        Map<Long, OrderUserResponseDto> orderItemsMap = new HashMap<>();
        for (Order order : orders) {
            Long order_id = order.getId();
            OrderUserResponseDto orderUserResponseDto = new OrderUserResponseDto();
            orderUserResponseDto.setAmount(order.getAmount());
            orderUserResponseDto.setDate(order.getDate());

            List<OrderItem> order_items = orderItemRepository.findAllByOrder_Id(order_id);
            List<OrderItemsUserResponseDto> orderItemsDtos = order_items.stream().map(orderItem -> {
                OrderItemsUserResponseDto itemDto = new OrderItemsUserResponseDto();
                itemDto.setCategory(orderItem.getCategory());
                itemDto.setDescription(orderItem.getDescription());
                itemDto.setName(orderItem.getName());
                itemDto.setImage(orderItem.getImage());
                itemDto.setPrice(orderItem.getPrice());
                itemDto.setQuantity(orderItem.getQuantity());
                itemDto.setSeller_id(orderItem.getSeller().getId());
                return itemDto;
            }).toList();

            orderUserResponseDto.setOrder_items(orderItemsDtos);
            orderItemsMap.put(order_id, orderUserResponseDto);
        }

        return orderItemsMap;
    }
}
