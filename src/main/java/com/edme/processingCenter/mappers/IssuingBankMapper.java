package com.edme.processingCenter.mappers;

import com.edme.processingCenter.dto.IssuingBankDto;
import com.edme.processingCenter.models.IssuingBank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IssuingBankMapper {
    IssuingBank toEntity(IssuingBankDto issuingBankDto);
    IssuingBankDto toDto(IssuingBank issuingBank);
}
