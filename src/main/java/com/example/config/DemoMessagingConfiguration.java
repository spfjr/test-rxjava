package com.example.config;

import java.util.concurrent.TimeUnit;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Shaun Fleming
 */
@Configuration
@EnableJms
public class DemoMessagingConfiguration implements JmsListenerConfigurer
{
    private static final String BROKER_URL = "tcp://usvaolmqsbx01.inf.videologygroup.com:61616";

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate()
    {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(60));

        final JmsMessagingTemplate lpMessagingTemplate = new JmsMessagingTemplate(connectionFactory());
        lpMessagingTemplate.setMessageConverter(jsonJackson2MessageConverter());
        lpMessagingTemplate.setJmsTemplate(jmsTemplate);

        return lpMessagingTemplate;
    }

    // --------------------------------------------

    @Bean
    public ValidatorFactory validatorFactory()
    {
        return Validation.buildDefaultValidatorFactory();
    }

    @Bean(destroyMethod = "stop")
    public PooledConnectionFactory connectionFactory()
    {
        final PooledConnectionFactory connectionFactory = new PooledConnectionFactory(BROKER_URL);
        connectionFactory.setIdleTimeout(0);
        return connectionFactory;
    }

    @Bean
    public ObjectMapper jsonObjectMapper()
    {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /**
     * Creates a JSON message converter that only accepts messages with a mime type of "application/json".
     *
     * @return New Message converter
     */
    @Bean
    public MessageConverter jsonJackson2MessageConverter()
    {
        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(jsonObjectMapper());
        return converter;
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory()
    {
        final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(jsonJackson2MessageConverter());
        return factory;
    }

    // The default listener container factory used by @JmsListener if not explicitly passed
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory()
    {
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        //factory.setErrorHandler(optMessagingErrorHandler());
        return factory;
    }

    @Override
    public void configureJmsListeners(final JmsListenerEndpointRegistrar registrar)
    {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}
