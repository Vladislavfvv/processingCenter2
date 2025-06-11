package com.edme.processingCenter.config;

import com.edme.common.exceptions.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder() {
            @Override
            public Exception decode(String methodKey, Response response) {
                HttpStatus status = HttpStatus.valueOf(response.status());
                
                if (status.is5xxServerError()) {
                    return new ServerErrorException("Server error occurred: " + status.value());
                }
                
                if (status.is4xxClientError()) {
                    return new ClientErrorException("Client error occurred: " + status.value());
                }
                
                if (status.is2xxSuccessful() && response.body() == null) {
                    return new EmptyResponseException("Empty response received");
                }
                
                return new RemoteServiceException("Unknown error occurred");
            }
        };
    }
} 