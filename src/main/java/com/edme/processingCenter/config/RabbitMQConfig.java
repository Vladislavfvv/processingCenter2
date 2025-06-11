package com.edme.processingCenter.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {

//    public static final String TRANSACTION_EXCHANGE = "transaction.exchange";
//    public static final String TRANSACTION_QUEUE = "transaction.queue";
//    public static final String TRANSACTION_ROUTING_KEY = "transaction.routing.key";
//
//    // создаём DirectExchange, который маршрутизирует по точному совпадению routing key
//    @Bean
//    public DirectExchange transactionExchange() {
//        return new DirectExchange(TRANSACTION_EXCHANGE);
//    }
//
//    // создаём durable очередь, в которой будут лежать сообщения
//    //   + «dead-letter» параметры — если потребитель не сможет обработать сообщение,
//    //     оно перейдёт в DLX (dead letter exchange) с ключом dlx.routing.key
//    @Bean
//    public Queue transactionQueue() {
//        return QueueBuilder.durable(TRANSACTION_QUEUE)
//                .withArgument("x-dead-letter-exchange", "dlx.exchange")
//                .withArgument("x-dead-letter-routing-key", "dlx.routing.key")
//                .build();
//    }
//    // связываем exchange и queue «чёрточкой» routing key
//    @Bean
//    public Binding transactionBinding() {
//        return BindingBuilder.bind(transactionQueue())
//                .to(transactionExchange())
//                .with(TRANSACTION_ROUTING_KEY);
//    }
//
//    // RabbitTemplate — компонент для отправки сообщений
//    //   + Jackson2JsonMessageConverter преобразует объекты в JSON-тело
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        return rabbitTemplate;
//    }
} 