package com.edme.processingCenter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "processingcenterschema")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_date", nullable = false )
    private Date transactionDate;
    @Column(name = "sum")
    private BigDecimal sum;
    @Column(name = "transaction_name", length = 255, nullable = false)
    private String transactionName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_type_id", referencedColumnName = "id", nullable = false)
    private TransactionType transactionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", referencedColumnName = "id", nullable = false)
    private Card card;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terminal_id", referencedColumnName = "id", nullable = false)
    private Terminal terminal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_code_id", referencedColumnName = "id", nullable = false)
    private ResponseCode responseCode;
    @Column(name = "authorization_code", length = 6, nullable = false)
    private String authorizationCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "received_from_issuing_bank", nullable = false)
    private Timestamp receivedFromIssuingBank;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "sent_to_issuing_bank", nullable = false)
    private Timestamp sentToIssuingBank;
}
