package uz.devops.orderms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.devops.orderms.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}