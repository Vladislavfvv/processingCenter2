package com.edme.processingCenter.services.rabbitMQ;

import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Service
public class TestService {
//
//    @Value("${queue.name}")
//    private String queueName;
//
//    @Autowired
//    //private RabbitTemplate rabbitTemplate;
//    private AmqpTemplate amqpTemplate;
//
//    public void send(TransactionExchangeIbDto dto) {
//        amqpTemplate.convertAndSend(queueName, dto);
//    }
}
