package techno_express.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.entity.Product;
import techno_express.backend.service.ProductService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService svc;

    @Autowired
    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    // 1) Listar todos los productos
    @GetMapping
    public List<Product> all() {
        return svc.getAllProducts();
    }

    // 2) Obtener un producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product p = svc.getProductById(id);
        return ResponseEntity.ok(p);
    }

    // 3) Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product saved = svc.saveProduct(product);
        return ResponseEntity
                .created(URI.create("/api/products/" + saved.getId()))
                .body(saved);
    }

    // 4) Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Long id,
            @RequestBody Product payload
    ) {
        payload.setId(id);
        Product updated = svc.updateProduct(payload);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // 5) Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
