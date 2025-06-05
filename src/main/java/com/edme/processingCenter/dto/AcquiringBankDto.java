package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AcquiringBankDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    @NotBlank(message = "bic is required")
    @Size(min = 9, max = 9, message = "bic must be exactly 9 characters")
    private String bic;
    @NotBlank(message = "AcquiringBank name is required")
    @Size(max = 255, message = "AcquiringBank name must be at most 255 characters")
    private String abbreviatedName;
}
