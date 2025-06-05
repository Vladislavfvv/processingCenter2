package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.CurrencyDto;
import com.edme.processingCenter.models.Currency;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDto toDto(Currency currency);
    Currency toEntity(CurrencyDto dto);
}
