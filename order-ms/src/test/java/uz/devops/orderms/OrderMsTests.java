package uz.devops.orderms;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import uz.devops.orderms.dto.ProductDTO;
import uz.devops.orderms.dto.ResponseDTO;
import uz.devops.orderms.service.mq.ProductMQClient;
import uz.devops.orderms.simulation.ProductRepositorySimulation;

import java.math.BigDecimal;

@SpringBootTest(properties = "mq.product-ms=simulation")
@ActiveProfiles("test")
class OrderMsTests {

    @Autowired
    private ProductMQClient productMQClient;

    @Autowired
    private ProductRepositorySimulation productRepositorySimulation;

    @Test
    void shouldSuccessfullyGetProduct() {

        ResponseDTO<ProductDTO> response = productMQClient.getProductById(1L);

        Assertions.assertTrue(response.getSuccess());
        ProductDTO productDTO = response.getData();
        Assertions.assertNotNull(productDTO);
        Assertions.assertEquals(productDTO.getId(), 1L);
        Assertions.assertEquals(productDTO.getName(), "name");
        Assertions.assertEquals(productDTO.getPrice(), BigDecimal.valueOf(1000));
    }

}
