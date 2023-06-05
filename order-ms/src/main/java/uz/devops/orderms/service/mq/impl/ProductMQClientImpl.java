package uz.devops.orderms.service.mq.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import uz.devops.orderms.domain.Product;
import uz.devops.orderms.dto.ProductDTO;
import uz.devops.orderms.dto.ResponseDTO;
import uz.devops.orderms.service.artemis.ArtemisService;
import uz.devops.orderms.service.mq.ProductMQClient;

@Service
@Slf4j
@AllArgsConstructor
public class ProductMQClientImpl implements ProductMQClient {

    private final ArtemisService artemisService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseDTO<ProductDTO> getProductById(Long id) {
        log.debug("Product MQ client request to get productById " + id);
        Message responseMessage = jmsTemplate.sendAndReceive("get-product-by-id", session -> {
            TextMessage message = session.createTextMessage();
            message.setText("1");
            message.setJMSType(Long.class.getSimpleName());
            return message;
        });

        if (responseMessage == null) {
            throw new RuntimeException("Couldn't get product");
        }

        try {
            String jsonString = responseMessage.getBody(String.class);

            ProductDTO product = objectMapper.readValue(jsonString, ProductDTO.class);

            log.debug("Result getProductById: {}", product);

            if (product == null)
                return new ResponseDTO<>(false, "Product not found");
            return new ResponseDTO<>(true, "Product found", product);
        } catch (JMSException e) {
            log.error("Exception while getting all bills from BillMqClient client. Message: {}", e.getMessage());
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsProductById(Long id) {
        log.debug("Product MQ client request to get existsById " + id);

        Message responseMessage = artemisService.sendAndReceive("");
        String body;
        try {

            body = responseMessage.getBody(String.class);
            Boolean exists = objectMapper.readValue(body, Boolean.class);
            log.debug("Result existsProductById: {}", exists);
            return exists != null ;

        } catch (JMSException | JsonProcessingException e) {
            log.error("Exception while getting all bills from BillMqClient client. Message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
