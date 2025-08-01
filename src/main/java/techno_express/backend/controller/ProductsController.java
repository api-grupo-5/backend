package techno_express.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.dto.ProductDto;
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
    public ResponseEntity<?> get_all_products(@RequestAttribute("request_id") String request_id,
                                              HttpServletRequest request){
        logger.info(request_id + " - inicio de get_all_products");
        List<ProductDto> products = productService.get_all_products(request_id);
        logger.info(request_id + " - fin de get_all_products");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get_product_by_id(@PathVariable Long id,
                                               @RequestAttribute("request_id") String request_id,
                                               HttpServletRequest request){
        logger.info(request_id + " - inicio de get_product_by_id");
        ProductDto product = productService.get_product_by_id(request_id, id);
        logger.info(request_id + " - fin de get_product_by_id");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request, product);
    }

    @PostMapping(path = {"/", ""})
    public ResponseEntity<?> add_product(@RequestAttribute("request_id") String request_id,
                                         HttpServletRequest request,
                                         @RequestBody ProductDto product_dto){
        logger.info(request_id + " - inicio de add_product");
        productService.add_product(request_id, product_dto);
        logger.info(request_id + " - fin de add_product");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }

    @PutMapping(path = {"/", ""})
    public ResponseEntity<?> update_product(@RequestAttribute("request_id") String request_id,
                                            HttpServletRequest request,
                                            @RequestBody ProductDto product_dto){
        logger.info(request_id + " - inicio de update_product");
        productService.update_product(request_id, product_dto);
        logger.info(request_id + " - fin de update_product");
        return ResponseBuilder.buildResponse(HttpStatus.OK, "0200", "ok", request);
    }
}
