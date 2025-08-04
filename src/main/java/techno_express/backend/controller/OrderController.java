// src/main/java/techno_express/backend/controller/OrderController.java
package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import techno_express.backend.dto.OrderDto;
import techno_express.backend.service.OrderService;
import techno_express.backend.util.ResponseBuilder;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping(path = {"/", ""})
    public ResponseEntity<?> create_order(@RequestAttribute("request_id") String request_id,
                                          @RequestBody OrderDto orderDto,
                                          HttpServletRequest request) {
        logger.info(request_id + " - inicio de create_order");
        orderService.create_order(request_id, orderDto);
        logger.info(request_id + " - fin de create_order");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
