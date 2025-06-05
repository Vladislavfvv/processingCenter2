package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResponseCodeDto {
    private Long id;
    @NotBlank(message = "errorCode is required")
    @Size(min = 2, max = 2, message = "errorCode must be exactly 2 characters")
    private String errorCode;
    @NotBlank(message = "errorDescription name is required")
    @Size(max = 255, message = "errorDescription name must be at most 255 characters")
    private String errorDescription;
    @NotBlank(message = "errorLevel name is required")
    @Size(max = 255, message = "errorLevel name must be at most 255 characters")
    private String errorLevel;
}
