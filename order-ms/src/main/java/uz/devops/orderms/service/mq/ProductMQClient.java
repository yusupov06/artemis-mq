package uz.devops.orderms.service.mq;

import uz.devops.orderms.dto.ProductDTO;
import uz.devops.orderms.dto.ResponseDTO;

import java.util.Optional;

public interface ProductMQClient {
    ResponseDTO<ProductDTO> getProductById(Long id);
    boolean existsProductById(Long id);
}
