package uz.devops.orderms.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import uz.devops.orderms.domain.Order;
import uz.devops.orderms.domain.OrderItem;
import uz.devops.orderms.dto.OrderAddDto;
import uz.devops.orderms.dto.ProductDTO;
import uz.devops.orderms.dto.ResponseDTO;
import uz.devops.orderms.service.mq.ProductMQClient;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class OrderHelperService {

    private final ProductMQClient productMQClient;

    public void setOrderItems(List<OrderAddDto.OrderItemAddDto> orderItems, Order order) {
        List<OrderItem> orderItems1 = orderItems.stream()
                .map(orderItemAddDto -> {
                    ResponseDTO<ProductDTO> response = productMQClient.getProductById(orderItemAddDto.getProductId());
                    if (!response.getSuccess())
                        throw new RuntimeException("Product not found with id " + orderItemAddDto.getProductId());
                    ProductDTO productDTO = response.getData();
                    return new OrderItem(productDTO.getId(),
                            orderItemAddDto.getQuantity(),
                            productDTO.getPrice().multiply(BigDecimal.valueOf(orderItemAddDto.getQuantity())),
                            order);
                }).toList();
        order.setOrderedItems(orderItems1);
    }

    public BigDecimal calcOverAllPrice(Order order) {
        BigDecimal price = new BigDecimal(0);
        order.getOrderedItems()
                .stream().map(OrderItem::getOverallPrice)
                .forEach(price::add);
        return price;
    }
}
