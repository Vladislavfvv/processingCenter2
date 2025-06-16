package com.edme.processingCenter.config;

import com.edme.common.exceptions.EmptyResponseException;
import com.edme.common.exceptions.ServerErrorException;
import feign.FeignException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;


@Configuration
@EnableRetry
public class RetryConfig {
    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3) // количество повторов
                .fixedBackoff(100) // задержка между повторами (мс)
                .retryOn(FeignException.class)
                .retryOn(ServerErrorException.class)
                .retryOn(EmptyResponseException.class)
                .build();
    }
}
