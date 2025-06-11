package com.edme.processingCenter.config;

import lombok.Setter;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//for my tests
@Setter
@Configuration
public class RabbitConfig {
    @Value("${queue.name}")
    private String queueName;

//    @Bean
//    public TopicExchange testExchange() {
//        return new TopicExchange("test.exchange");
//    }

//    @Bean
//    public Queue testQueue() {
//        return new Queue("test.queue", false);
//    }

        @Bean
    public Queue testQueue() {
        return new Queue(queueName, false);
    }

//    @Bean
//    public Binding binding(Queue testQueue, TopicExchange testExchange) {
//        return BindingBuilder.bind(testQueue).to(testExchange).with("test.routing.key");
//    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
