package com.edme.processingCenter.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyDto {
    private String name;
    private LocalDateTime createdAt;
}
