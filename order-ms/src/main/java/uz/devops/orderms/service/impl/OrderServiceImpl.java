package uz.devops.orderms.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.devops.orderms.domain.Order;
import uz.devops.orderms.dto.OrderAddDto;
import uz.devops.orderms.repository.OrderRepository;
import uz.devops.orderms.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderHelperService orderHelperService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find order"));
    }

    @Override
    public Order addOrder(OrderAddDto addDto) {
        Order order = new Order();
        orderHelperService.setOrderItems(addDto.getOrderItems(), order);
        BigDecimal overAllPrice = orderHelperService.calcOverAllPrice(order);
        order.setOverAllPrice(overAllPrice);
        return order;
    }
}
