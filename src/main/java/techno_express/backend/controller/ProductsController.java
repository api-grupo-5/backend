package techno_express.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.entity.Product;
import techno_express.backend.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(@RequestAttribute("request_id") String request_id){
        System.out.println(request_id + " - inicio de getAllProducts");
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id, @RequestAttribute("request_id") String request_id){
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product, @RequestAttribute("request_id") String request_id){
        return productService.saveProduct(product);
    }

    @PutMapping
    Product updateProduct(@RequestBody Product product, @RequestAttribute("request_id") String request_id){
        return productService.updateProduct(product);
    }
}
