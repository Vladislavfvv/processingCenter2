package com.edme.processingCenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrencyDto {

    @Schema(description = "Unique identifier of the currency", example = "1")
    private Long id;

    @NotBlank(message = "currencyDigitalCode is required")
    @Size(max = 3, message = "currencyDigitalCode must be at most 3 characters")
    @Schema(description = "Numeric currency code (e.g., 840 for USD)", example = "840")
    private String currencyDigitalCode;

    @NotBlank(message = "currencyLetterCode is required")
    @Size(max = 3, message = "currencyLetterCode must be at most 3 characters")
    @Schema(description = "Alphabetic currency code (e.g., USD)", example = "USD")
    private String currencyLetterCode;

    @NotBlank(message = "currencyDigitalCodeAccount is required")
    @Size(max = 3, message = "currencyDigitalCodeAccount must be at most 3 characters")
    @Schema(description = "Alternative numeric currency code for accounting", example = "840")
    private String currencyDigitalCodeAccount;

    @NotBlank(message = "currencyName is required")
    @Size(max = 255, message = "currencyName must be at most 255 characters")
    @Schema(description = "Currency name", example = "Dollar USA")
    private String currencyName;
}
