package com.edme.processingCenter.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CallbackRabbitConfig {
    @Value("${callback.queue}")
    private String callbackQueue;

    @Bean
    public Queue callbackQueue() {
        return new Queue(callbackQueue, false);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory callbackListenerFactory(ConnectionFactory connectionFactory,
                                                                        MessageConverter messageConverter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
