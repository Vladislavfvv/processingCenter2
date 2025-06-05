package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "currency", schema = "processingcenterschema")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "currency_digital_code", length = 3, nullable = false)
    private String currencyDigitalCode;
    @Column(name = "currency_letter_code", length = 3, nullable = false)
    private String currencyLetterCode;
    @Column(name = "currency_digital_code_account", length = 3, nullable = false)
    private String currencyDigitalCodeAccount;
    @Column(name = "currency_name", length = 255, nullable = false, unique = true)
    private String currencyName;
}


