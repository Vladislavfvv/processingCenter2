package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TerminalDto {
    private Long id;
    @NotBlank(message = "terminalId is required")
    @Size(min = 9, max = 9, message = "terminalId must be exactly 9 characters")
    private String terminalId;
    @NotNull(message = "merchantCategoryCodeId ID is required")
   // private Long merchantCategoryCodeId;
    private MerchantCategoryCodeDto mcc;
    @NotNull(message = "SalesPointId ID is required")
    private SalesPointDto salesPoint;
}
