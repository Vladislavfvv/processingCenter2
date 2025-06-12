package com.edme.processingCenter.services.feign;

import com.edme.commondto.dto.TransactionExchangeDto;
import com.edme.processingCenter.client.SalesPointClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SalesPointFallback implements SalesPointClient {
    private static final Logger log = LoggerFactory.getLogger(SalesPointFallback.class);

    @Override
    public TransactionExchangeDto sendTransaction(TransactionExchangeDto dto) {
        log.error("SalesPoint недоступен. Возвращаем fallback.");
        TransactionExchangeDto fallback = new TransactionExchangeDto();
        fallback.setAuthorizationCode("FAILED");
        fallback.setId(null);
        return fallback;
    }
}
