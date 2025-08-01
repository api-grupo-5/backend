package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.*;
import techno_express.backend.service.CartService;
import techno_express.backend.util.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<?> load_cart(@RequestAttribute("request_id") String request_id,
                                       @RequestBody CartDto cartDto,
                                       @PathVariable Long id,
                                       HttpServletRequest request) {

        logger.info(request_id + " - inicio de load_cart");
        HashMap<String, Object> result = cartService.load_cart(request_id, cartDto, id);
        logger.info(request_id + " - fin de load_cart");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> save_cart(@RequestAttribute("request_id") String request_id,
                                       @RequestBody CartDto cart_dto,
                                       @PathVariable Long id,
                                       HttpServletRequest request) {

        logger.info(request_id + " - inicio de save_cart");
        cartService.save_cart(request_id, cart_dto, id);
        logger.info(request_id + " - fin de save_cart");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
