package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SalesPointDto {
    private Long id;
    @NotBlank(message = "posName name is required")
    @Size(max = 255, message = "posName name must be at most 255 characters")
    private String posName;
    @NotBlank(message = "posAddress name is required")
    @Size(max = 255, message = "posAddress name must be at most 255 characters")
    private String posAddress;
    @NotBlank(message = "posInn is required")
    @Size(min = 12, max = 12, message = "posInn must be exactly 12 characters")
    private String posInn;
    //    @NotNull(message = "paymentSystemId ID is required")
    //    private Long acquiringBankId;
    @NotNull(message = "AcquiringBankId ID is required")
    private AcquiringBankDto acquiringBank;
}
