package uz.devops.orderms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.devops.orderms.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}