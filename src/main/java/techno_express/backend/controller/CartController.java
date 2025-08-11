package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.context.RequestContext;
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
    public ResponseEntity<?> load_cart(@RequestBody CartDto cartDto,
                                       @PathVariable Long id,
                                       HttpServletRequest request) {
        String request_id = RequestContext.getRequestId();

        logger.info(request_id + " - inicio de load_cart");
        HashMap<String, Object> result = cartService.load_cart(request_id, cartDto, id);
        logger.info(request_id + " - fin de load_cart");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> save_cart(@RequestBody CartDto cart_dto,
                                       @PathVariable Long id,
                                       HttpServletRequest request) {

        String request_id = RequestContext.getRequestId();
        logger.info(request_id + " - inicio de save_cart");
        cartService.save_cart(request_id, cart_dto, id);
        logger.info(request_id + " - fin de save_cart");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete_cart(@RequestBody CartDto cartDto,
                                         @PathVariable Long id,
                                         HttpServletRequest request) {

        String request_id = RequestContext.getRequestId();
        logger.info(request_id + " - inicio de delete_cart");
        cartService.delete_cart(request_id, cartDto, id);
        logger.info(request_id + " - fin de delete_cart");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
