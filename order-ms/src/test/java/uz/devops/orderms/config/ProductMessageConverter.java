package uz.devops.orderms.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

public class ProductMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

    public ProductMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public @NotNull Message toMessage(@NotNull Object object, Session session) throws JMSException, MessageConversionException {
        try {
            String jsonPayload = objectMapper.writeValueAsString(object);
            return session.createTextMessage(jsonPayload);
        } catch (JsonProcessingException e) {
            throw new MessageConversionException("Failed to convert object to JMS message", e);
        }
    }

    @Override
    public @NotNull String fromMessage(@NotNull Message message) throws JMSException, MessageConversionException {
        return ((TextMessage) message).getText();
    }
}