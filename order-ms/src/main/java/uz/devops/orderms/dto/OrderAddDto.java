package uz.devops.orderms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddDto {
    private List<OrderItemAddDto> orderItems;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class OrderItemAddDto {
        private Long productId;
        private Integer quantity;
    }
}
