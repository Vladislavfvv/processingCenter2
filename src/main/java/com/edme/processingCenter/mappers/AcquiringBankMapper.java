package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.AcquiringBankDto;
import com.edme.processingCenter.models.AcquiringBank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcquiringBankMapper {
    AcquiringBankDto toDto(AcquiringBank entity);
    AcquiringBank toEntity(AcquiringBankDto dto);

}
