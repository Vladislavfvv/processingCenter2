package com.edme.processingCenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MerchantCategoryCodeDto {
    private Long id;
    @NotBlank(message = "mcc is required")
    @Size(min = 4, max = 4, message = "mcc must be exactly 4 characters")
    private String mcc;
    @NotBlank(message = "mccName name is required")
    @Size(max = 255, message = "mccName name must be at most 255 characters")
    private String mccName;
}
