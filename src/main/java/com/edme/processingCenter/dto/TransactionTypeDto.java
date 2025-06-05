package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransactionTypeDto {
    private Long id;
    @NotBlank(message = "transactionTypeName is required")
    @Size(max = 255, message = "transactionTypeName must be at most 255 characters")
    private String transactionTypeName;
    @NotBlank(message = "operator is required")
    @Size(min = 1, max = 1, message = "operator must be exactly 1 characters")
    private String operator;
}
