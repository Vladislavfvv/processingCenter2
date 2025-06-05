package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "merchant_category_code", schema = "processingcenterschema")
public class MerchantCategoryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "mcc", length = 4, nullable = false)
    private String mcc;
    @Column(name = "mcc_name", length = 255, nullable = false)
    private String mccName;
}

