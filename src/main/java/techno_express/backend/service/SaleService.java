package techno_express.backend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import techno_express.backend.dto.SaleRequestDto;
import techno_express.backend.entity.Product;
import techno_express.backend.entity.Sale;
import techno_express.backend.repository.SaleRepository;
import techno_express.backend.service.ProductService;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    @Autowired
    public SaleService(SaleRepository saleRepository, ProductService productService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    public Sale createSale(SaleRequestDto dto) {
        // 1. Obtener el producto
        Product prod = productService.getProductById(dto.getProductId());

        // 2. Crear la entidad Sale
        Sale sale = new Sale();
        sale.setProduct(prod);
        sale.setQuantity(dto.getQuantity());
        sale.setBuyer(dto.getBuyer());
        // timestamp se pone automáticamente con @CreationTimestamp

        // 3. Guardar
        return saleRepository.save(sale);
    }
}
