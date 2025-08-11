// src/main/java/techno_express/backend/controller/OrderController.java
package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.OrderItemsUserResponseDto;
import techno_express.backend.dto.OrderRequestDto;
import techno_express.backend.dto.OrderUserResponseDto;
import techno_express.backend.entity.OrderItem;
import techno_express.backend.service.OrderService;
import techno_express.backend.util.ResponseBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping(path = {"/", ""})
    public ResponseEntity<?> create_order(@RequestAttribute("request_id") String request_id,
                                          @RequestBody OrderRequestDto orderRequestDto,
                                          HttpServletRequest request) {
        logger.info(request_id + " - inicio de create_order");
        orderService.create_order(request_id, orderRequestDto);
        logger.info(request_id + " - fin de create_order");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @GetMapping(path = {"/get-user-orders", ""})
    public ResponseEntity<?> get_order_by_user_id(@RequestAttribute("request_id") String request_id,
                                                  @RequestBody OrderRequestDto orderRequestDto,
                                                  HttpServletRequest request) {
        logger.info(request_id + " - inicio de get_order_by_user_id");
        Map<Long, OrderUserResponseDto> orders = orderService.get_order_by_user_id(request_id, orderRequestDto);
        logger.info(request_id + " - fin de get_order_by_user_id");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, orders);
    }
}
