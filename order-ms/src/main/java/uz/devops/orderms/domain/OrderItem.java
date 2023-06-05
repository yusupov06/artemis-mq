package uz.devops.orderms.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal overallPrice;
    @ManyToOne
    private Order order;

    public OrderItem(Long productId, Integer quantity, BigDecimal overallPrice, Order order) {
        this.productId = productId;
        this.quantity = quantity;
        this.overallPrice = overallPrice;
        this.order = order;
    }
}
