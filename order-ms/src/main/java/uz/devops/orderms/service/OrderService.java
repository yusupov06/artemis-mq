package uz.devops.orderms.service;

import uz.devops.orderms.domain.Order;
import uz.devops.orderms.dto.OrderAddDto;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order addOrder(OrderAddDto addDto);

}
