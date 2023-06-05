package uz.devops.orderms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.jms.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MessageConverter;

@Configuration
@EnableJms
@Slf4j
@AllArgsConstructor
public class ArtemisConfig {


    @PostConstruct
    public void init() {
        log.info("Initializing ArtemisConfig");
    }

    private final ApplicationContext applicationContext;

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setConcurrency("2-4");
        MessageConverter messageConverter = applicationContext.getBean("ORDER_MS_MESSAGE_CONVERTER", MessageConverter.class);
        jmsListenerContainerFactory.setMessageConverter(messageConverter);
        return jmsListenerContainerFactory;
    }

    @Bean("ORDER_MS_MESSAGE_CONVERTER")
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new ProductMessageConverter(objectMapper);
    }
}
