package com.edme.processingCenter.models;

import lombok.*;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users", schema = "processingcenterschema",
        uniqueConstraints = @UniqueConstraint(columnNames = {"first_name", "last_name"})) // Уникальность для нескольких полей  // Уникальность для нескольких полей
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;
}
