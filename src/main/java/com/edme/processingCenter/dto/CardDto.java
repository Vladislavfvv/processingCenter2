package com.edme.processingCenter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardDto {
    @Schema(description = "Unique account identifier", example = "123")
    private Long id;

    @NotNull(message = "CardNumber is required")
    @Size(max = 50, message = "CardNumber must be at most 50 characters")
    @Schema(description = "cardNumber (must not be more 50 symbols)", example = "5123450000000024")
    private String cardNumber;

    @NotNull(message="Expiration date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Card expiration date", example = "2025-12-31")
    private Date expirationDate;

    @NotNull(message = "Holder name is required")
    @Size(max = 50, message = "Holder name must be at most 50 characters")
    @Schema(description = "Card holder's full name", example = "Ivan Ivanov")
    private String holderName;

    @NotNull(message = "Card status ID is required")
    @Schema(description = "Reference to the card status", example = "1")
    private CardStatusDto cardStatus;

    @NotNull(message = "Payment system ID is required")
    @Schema(description = "Reference to the payment system", example = "2")
    private PaymentSystemDto paymentSystem;

    @NotNull(message = "Account ID is required")
    @Schema(description = "Reference to the account", example = "10")
    private AccountDto account;

    @NotNull(message = "Received from processing center timestamp is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Schema(description = "Timestamp when data was received from the IssuingBank", example = "2022-10-21 13:12:12.159")
    private Timestamp receivedFromIssuingBank;

    @NotNull(message = "Sent to processing center timestamp is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Schema(description = "Timestamp when data was sent to the IssuingBank", example = "2022-10-21 13:12:12.159")
    private Timestamp sentToIssuingBank;



}
