package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "sales_point", schema = "processingcenterschema")
public class SalesPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pos_name", length = 255, nullable = false)
    private String posName;
    @Column(name = "pos_address", length = 255, nullable = false)
    private String posAddress;
    @Column(name = "pos_inn", length = 12, nullable = false)
    private String posInn;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acquiring_bank_id", referencedColumnName = "id", nullable = false)
    public AcquiringBank acquiringBank; //ok
}
