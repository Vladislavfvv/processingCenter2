package com.edme.processingCenter.services.rabbitMQ;

import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j

public class CallbackListener {
    @RabbitListener(queues = "${callback.queue}")
    public void receiveCallback(TransactionExchangeIbDto response) {
        log.info("<<< Получен callback от Consumer:");
        log.info("id: {}", response.getId());
        log.info("CreatedAt: {}", response.getTransactionDate());
        log.info("Name: {}", response.getTransactionName());
        log.info("Account: {}", response.getAccount());
        log.info("sum: {}", response.getSum());
        //log.info("sum: {}", response.getReceivedFromProcessingCenter());
    }
}
