// src/main/java/techno_express/backend/controller/OrderController.java
package techno_express.backend.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import techno_express.backend.dto.OrderDto;
import techno_express.backend.service.OrderService;

@RestController
@RequestMapping("/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestAttribute("request_id") String request_id,
            @RequestBody OrderDto orderDto) {
        logger.info(request_id + " - inicio de createOrder");
        OrderDto saved = orderService.createOrder(orderDto);
        logger.info(request_id + " - fin de createOrder");
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(@RequestAttribute("request_id") String request_id) {
        logger.info(request_id + " - inicio de getAllOrders");
        List<OrderDto> orders = orderService.getAllOrders();
        logger.info(request_id + " - fin de getAllOrders");
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @RequestAttribute("request_id") String request_id,
            @PathVariable Long id) {
        logger.info(request_id + " - inicio de getOrderById");
        OrderDto order = orderService.getOrderById(id);
        logger.info(request_id + " - fin de getOrderById");
        return ResponseEntity.ok(order);
    }
}
