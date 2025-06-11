package com.edme.processingCenter.dto;

import com.edme.processingCenter.models.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
//public class TransactionDto {
//    private Long id;
//    private LocalDate transactionDate;
//    private BigDecimal sum;
//    private Long transactionTypeId;
//    private Long cardId;
//    private Long terminalId;
//    private Long responseCodeId;
//    @NotBlank(message = "terminalId is required")
//    @Size(min = 6, max = 6, message = "terminalId must be exactly 6 characters")
//    private String authorizationCode;
//}
public class TransactionDto {
    private Long id;
    private LocalDate transactionDate;
    private BigDecimal sum;
    private String transactionName;
    private AccountDto account;
    @NotNull(message = "TransactionType id is required")
    private TransactionTypeDto transactionType;
    @NotNull(message = "Card id is required")
    private CardDto card;
    @NotNull(message = "Terminal id is required")
    private TerminalDto terminal;
    @NotNull(message = "ResponseCode id is required")
    private ResponseCodeDto responseCode;
    @NotBlank(message = "authorizationCode is required")
    @Size(min = 6, max = 6, message = "authorizationCode must be exactly 6 characters")
    private String authorizationCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp receivedFromIssuingBank;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp sentToIssuingBank;
}

