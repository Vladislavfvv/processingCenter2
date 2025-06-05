package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "terminal", schema = "processingcenterschema")
public class Terminal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "terminal_id", length = 9, nullable = false)
    private String terminalId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcc_id", referencedColumnName = "id", nullable = false)
    private MerchantCategoryCode mcc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_id", referencedColumnName = "id", nullable = false)
    private SalesPoint pos;

}
