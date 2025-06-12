package techno_express.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techno_express.backend.entity.Sale;
import techno_express.backend.repository.SaleRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleRepository saleRepo;

    public SaleController(SaleRepository saleRepo) {
        this.saleRepo = saleRepo;
    }

    // 1) Listar todas las ventas
    @GetMapping
    public List<Sale> listSales() {
        return saleRepo.findAll();
    }

    // 2) Registrar una nueva venta
    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        Sale saved = saleRepo.save(sale);
        return ResponseEntity
                .created(URI.create("/api/sales/" + saved.getId()))
                .body(saved);
    }
}
