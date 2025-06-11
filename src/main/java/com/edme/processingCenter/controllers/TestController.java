package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import com.edme.processingCenter.services.rabbitMQ.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/test")
public class TestController {
//    private final RabbitTemplate rabbitTemplate;
//
//    public TestController(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> sendMessage(@RequestBody MyDto dto) {
//        rabbitTemplate.convertAndSend("test.exchange", "test.routing.key", dto);
//        return ResponseEntity.ok("Message sent");
//    }

    private final TestService testService;

//    @PostMapping("/sent")
//    public ResponseEntity<?> test(@RequestBody TransactionExchangeIbDto myDto) {
//        if (myDto.getTransactionName().isBlank()) {
//            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
//        }
//         testService.send(myDto);
//        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
//    }
}
