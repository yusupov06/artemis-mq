package uz.devops.orderms.config;

import jakarta.annotation.PostConstruct;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

@Configuration
@EnableJms
@Profile("!test")
@Slf4j
public class MQConfig {

    @Value("${spring.artemis.broker-url}")
    String BROKER_URL;
    @Value("${spring.artemis.user}")
    String BROKER_USERNAME;
    @Value("${spring.artemis.password}")
    String BROKER_PASSWORD;

    @PostConstruct
    public void init() throws Exception {
      log.info("Initializing MQConfig");
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(BROKER_URL);
        connectionFactory.setPassword(BROKER_USERNAME);
        connectionFactory.setUser(BROKER_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate() throws JMSException {
        JmsTemplate template = new JmsTemplate();
        template.setDefaultDestination(new ActiveMQQueue("topic/department-ms"));
        template.setConnectionFactory(connectionFactory());
        template.setPubSubDomain(true);
        template.setDestinationResolver(destinationResolver());
        template.setDeliveryPersistent(true);
        return template;
    }

    @Bean
    DynamicDestinationResolver destinationResolver() {
        return new DynamicDestinationResolver() {
            @Override
            public Destination resolveDestinationName(Session session, String destinationName, boolean pubSubDomain) throws JMSException {
                pubSubDomain = destinationName.startsWith("topic");
                return super.resolveDestinationName(session, destinationName, pubSubDomain);
            }
        };
    }


}
