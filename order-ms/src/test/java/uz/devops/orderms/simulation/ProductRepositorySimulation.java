package uz.devops.orderms.simulation;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import uz.devops.orderms.dto.ProductDTO;

import java.math.BigDecimal;

@Component
@ConditionalOnProperty(
        prefix = "mq",
        name = "product-ms",
        havingValue = "simulation"
)
@Slf4j
public class ProductRepositorySimulation {

    @PostConstruct
    public void init() {
        System.out.println("ProductRepositorySimulation init");
    }

    @JmsListener(destination = "get-product-by-id", selector = "JMSType='Long'")
    public ProductDTO getProductById(Long id) {
        log.info("getProductById");
        return new ProductDTO(1L, "name", BigDecimal.valueOf(1000));
    }

}
