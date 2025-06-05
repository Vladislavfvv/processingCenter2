package com.edme.processingCenter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {
    @Schema(description = "Unique account identifier", example = "123")
    private Long id;

    @NotNull(message = "Account number is required")
    @Pattern(regexp = "^\\d{20}$", message = "Account number must be exactly 20 digits")
    @Schema(description = "Account number (exactly 20 digits)", example = "40817810800000000001")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 12, fraction = 2, message = "Balance must be in the format of up to 12 digits and 2 decimal places")
    @Schema(description = "Account balance", example = "1000.50")
    private BigDecimal balance;

    @NotNull(message = "Currency ID is required")
    @Schema(description = "Currency identifier", example = "1")
    private CurrencyDto currency;

    @NotNull(message = "IssuingBank ID is required")
    @Schema(description = "IssuingBank identifier", example = "1")
    private IssuingBankDto issuingBank;
}
