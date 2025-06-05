package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PaymentSystemDto {
    Long id;
    @NotBlank(message = "paymentSystemName is required")
    @Size(max = 50, message = "paymentSystemName must be at most 50 characters")
    String paymentSystemName;
}
