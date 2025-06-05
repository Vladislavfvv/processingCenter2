package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.CardStatusDto;
import com.edme.processingCenter.models.CardStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardStatusMapper {
    CardStatusDto toDto(CardStatus cardStatus);
    CardStatus toEntity(CardStatusDto cardStatusDto);
}
