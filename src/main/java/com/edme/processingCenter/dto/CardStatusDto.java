package com.edme.processingCenter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardStatusDto {

    @Schema(description = "Unique account identifier", example = "123")
    private Long id;

    @Size(max = 255, message = "cardStatusName must be at most 255 characters")
    @Schema(description = "cardStatusName state name", example = "Активна")
    private String cardStatusName; //Название статуса карты (например, "Активна", "Заблокирована").
}
