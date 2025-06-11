package com.edme.processingCenter.services.rabbitMQ;

import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Service
public class IssuingBankClientService {
    @Value("${queue.name}")
    private String queueName;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendTransactionWithRabbitMq(TransactionExchangeIbDto transactionExchangeIbDto) {
        amqpTemplate.convertAndSend(queueName, transactionExchangeIbDto);
    }
}
