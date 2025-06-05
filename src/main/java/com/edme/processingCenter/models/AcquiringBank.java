package com.edme.processingCenter.models;

import lombok.*;


import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "acquiring_bank", schema = "processingcenterschema")
public class AcquiringBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bic", length = 9, nullable = false)
    private String bic;
    @Column(name = "abbreviated_name", length = 255, nullable = false)
    private String abbreviatedName;
}
