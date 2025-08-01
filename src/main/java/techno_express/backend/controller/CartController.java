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

    @PostMapping("/saveCart")
    public ResponseEntity<?> save_cart(@RequestAttribute("request_id") String request_id,
                                      @RequestBody CartDto cartDto,
                                      HttpServletRequest request) {

        logger.info(request_id + " - inicio de guardado de carrito");
        cartService.save_cart(request_id, cartDto);
        logger.info(request_id + " - fin de guardado de carrito");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PostMapping("/loadCart")
    public ResponseEntity<?> load_cart(@RequestAttribute("request_id") String request_id,
                                       @RequestBody CartDto cartDto,
                                       HttpServletRequest request) {

        logger.info(request_id + " - inicio de obtencion de carrito");
        HashMap<String, Object> result = cartService.load_cart(request_id, cartDto);
        logger.info(request_id + " - fin de obtencion de carrito");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, result);
    }
}
