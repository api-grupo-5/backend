package techno_express.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techno_express.backend.dto.OrderDto;
import techno_express.backend.entity.Order;
import techno_express.backend.entity.OrderItem;
import techno_express.backend.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // 1) Mapear DTO a entidad
        Order order = new Order();
        order.setAmount(orderDto.getAmount());
        // Asociar cada OrderItem a esta Order
        for (OrderItem item : orderDto.getItems()) {
            item.setOrder(order);
        }
        order.setItems(orderDto.getItems());

        // 2) Guardar
        Order saved = orderRepository.save(order);

        // 3) Mapear de vuelta a DTO
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found with id " + id));
        return mapToDto(order);
    }

    /** Mapea entidad → DTO */
    private OrderDto mapToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setItems(order.getItems());
        dto.setAmount(order.getAmount());
        return dto;
    }
}
