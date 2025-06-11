package com.edme.processingCenter.config;

import com.edme.common.exceptions.ClientErrorException;
import com.edme.common.exceptions.EmptyResponseException;
import com.edme.common.exceptions.ServerErrorException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRetry
public class RetryConfig {
//    @Bean
//    public RetryTemplate retryTemplate() {
//        return RetryTemplate.builder()
//                .maxAttempts(3)
//                .fixedBackoff(100)
//                .retryOn(ServerErrorException.class)
//                .retryOn(EmptyResponseException.class)
//                .build();
//    }

    @Bean
    public RetryTemplate retryTemplate() {
        Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>();
        retryableExceptions.put(ServerErrorException.class, true);
        retryableExceptions.put(EmptyResponseException.class, true);
        retryableExceptions.put(ClientErrorException.class, false);

        return RetryTemplate.builder()
                .customPolicy(new SimpleRetryPolicy(3, retryableExceptions, true))
                .fixedBackoff(100)
                .build();
    }
}
