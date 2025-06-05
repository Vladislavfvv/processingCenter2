package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "issuing_bank", schema = "processingcenterschema")
public class IssuingBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bic", length = 9, nullable = false)
    private String bic;
    @Column(name = "bin", length = 5, nullable = false)
    private String bin;
    @Column(name = "abbreviated_name", length = 255, nullable = false)
    private String abbreviatedName;
}
