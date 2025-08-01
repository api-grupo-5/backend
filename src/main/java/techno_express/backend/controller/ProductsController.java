package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.ProductDto;
import techno_express.backend.entity.Product;
import techno_express.backend.service.ProductService;
import techno_express.backend.util.ResponseBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @Autowired
    private ProductService productService;

    @GetMapping(path = {"/", ""})
    public ResponseEntity<?> getAllProducts(@RequestAttribute("request_id") String request_id,
                                            HttpServletRequest request){
        logger.info(request_id + " - inicio de getAllProducts");
        List<ProductDto> products = productService.getAllProducts(request_id);
        logger.info(request_id + " - fin de getAllProducts");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id,
                                            @RequestAttribute("request_id") String request_id,
                                            HttpServletRequest request){
        logger.info(request_id + " - inicio de getProductById");
        ProductDto product = productService.getProductById(request_id, id);
        logger.info(request_id + " - fin de getProductById");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, product);
    }

    @PostMapping(path = {"/", ""})
    public ResponseEntity<?> createProduct(@RequestAttribute("request_id") String request_id,
                                           HttpServletRequest request,
                                           @RequestBody ProductDto productDto){
        logger.info(request_id + " - inicio de createProduct");
        productService.createProduct(request_id, productDto);
        logger.info(request_id + " - fin de createProduct");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PutMapping(path = {"/", ""})
    public ResponseEntity<?> updateProduct(@RequestAttribute("request_id") String request_id,
                                           HttpServletRequest request,
                                           @RequestBody ProductDto productDto){
        logger.info(request_id + " - inicio de updateProduct");
        productService.updateProduct(request_id, productDto);
        logger.info(request_id + " - fin de updateProduct");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
