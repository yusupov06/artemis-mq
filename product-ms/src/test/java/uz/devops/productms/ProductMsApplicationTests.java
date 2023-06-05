package uz.devops.productms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uz.devops.productms.domain.Product;
import uz.devops.productms.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
class ProductMsApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        productRepository.saveAll(
                Arrays.asList(
                        new Product(1L, "1", BigDecimal.valueOf(1000)),
                        new Product(2L, "2", BigDecimal.valueOf(1000)),
                        new Product(3L, "3", BigDecimal.valueOf(2000))
                )
        );
    }

}
