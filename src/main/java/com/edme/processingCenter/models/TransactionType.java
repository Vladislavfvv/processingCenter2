package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "transaction_type", schema = "processingcenterschema")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "transaction_type_name", length = 255, nullable = false)
    private String transactionTypeName;
    @Column(name = "operator", length = 1, nullable = false)
    private String operator;

}
