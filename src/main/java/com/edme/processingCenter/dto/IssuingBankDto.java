package com.edme.processingCenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class IssuingBankDto {
    Long id;
    @NotBlank(message = "bic is required")
    @Size(max = 9, message = "bic must be 9 characters")
    String bic;

    @NotBlank(message = "bin is required")
    @Size(max = 5, message = "bin must be 5 characters")
    String bin;

    @NotBlank(message = "abbreviatedName is required")
    @Size(max = 255, message = "abbreviatedName must not be more 255 characters")
    private String abbreviatedName;
}
