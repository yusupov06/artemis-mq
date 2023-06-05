package uz.devops.orderms.service.artemis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import uz.devops.orderms.dto.RequestData;
import uz.devops.orderms.dto.ResponseDTO;

import java.util.UUID;

@Slf4j
@Service
@EnableJms
@RequiredArgsConstructor
public class ArtemisService {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public MessageCreator createMessage(RequestData<?> data, String requestId) {
        log.debug("Artemis create message method with requestData: {}", data);
        return s -> createJmsMessage(data, requestId, s, objectMapper);
    }

    public MessageCreator createMessage(String requestId) {
        log.debug("Artemis create message method with requestId: {}", requestId);

        return s -> {
            Message responseMsg = s.createMessage();
            responseMsg.setJMSCorrelationID(requestId);
            return responseMsg;
        };
    }

    public Message createJmsMessage(String destinationName, Long id) {
        log.debug("sendAndReceive data to destination : {} by id: {}", destinationName, id);

        var result = jmsTemplate.sendAndReceive(destinationName, session -> {
            TextMessage message = session.createTextMessage();
            message.setStringProperty("sayHello", "send JMS text message");
            message.setText(String.valueOf(id));

            return message;
        });

        log.debug("Result get JMS DATA: {}", result);
        return result;
    }

    public Message createJmsMessage(RequestData<?> data, String requestId, Session s, ObjectMapper objectMapper) throws JMSException {
        Message responseMsg = s.createMessage();
        try {
            responseMsg = s.createTextMessage(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            log.error("Can not cast to string data: {}", e.getMessage());
        }
        responseMsg.setJMSCorrelationID(requestId);
        responseMsg.setJMSType(data.getClass().getSimpleName());

        return responseMsg;
    }

    private static String generateRequestID() {
        return String.format("%s_%s", UUID.randomUUID(), System.currentTimeMillis());
    }

    public void send(String destination, RequestData<?> data) {
        log.debug("sendAndReceive data to destination : {}, data: {}", destination, data);
        jmsTemplate.send(destination, createMessage(data, generateRequestID()));
    }

    public Message sendAndReceive(String destination) {
        log.debug("sendAndReceive data to destination : {}", destination);
        return jmsTemplate.sendAndReceive(destination, createMessage(generateRequestID()));
    }

    public Message sendAndReceive(String destination, RequestData<?> data) {
        log.debug("sendAndReceive data to destination : {}, data: {}", destination, data);
        return jmsTemplate.sendAndReceive(destination, createMessage(data, generateRequestID()));
    }

    public void callBackMessage(RequestData<?> data, Message message) throws JMSException {
        log.trace("JMS Call back Request data: {}, jmsId: {} ", data, message);
        jmsTemplate.send(message.getJMSReplyTo(), createMessage(data, message.getJMSCorrelationID()));
    }

    public ResponseDTO<?> sendAndReceiveResponse(String destination, RequestData<?> data) {
        log.debug("Request to send message and receive response. Request DATA: {} | DESTINATION: {}", data, destination);
        var response = sendAndReceive(destination, data);
        try {
            return objectMapper.readValue(response.getBody(String.class), new TypeReference<>() {
            });
        } catch (JsonProcessingException | JMSException e) {
            log.error(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
