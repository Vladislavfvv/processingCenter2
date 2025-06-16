package com.edme.processingCenter.config;

import com.edme.common.exceptions.*;
import feign.Response;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 3);
    }


    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            HttpStatus status = HttpStatus.resolve(response.status());
            System.err.printf("Feign error in method: %s, status: %d%n", methodKey, response.status());

            if (status == null) {
                return new RemoteServiceException("Unknown HTTP status");
            }

            if (status.is5xxServerError()) {
                // Ретрай сработает, потому что выбрасывается исключение
                return new ServerErrorException("Server error occurred: " + status.value());
            }

            if (status.is4xxClientError()) {
                // Не ретраим, но выбрасываем
                return new ClientErrorException("Client error occurred: " + status.value());
            }

            if (status.is2xxSuccessful() && response.body() == null) {
                // Будет ретраиться
                return new EmptyResponseException("Empty response received");
            }

            return new RemoteServiceException("Unhandled error");
        };
    }

} 