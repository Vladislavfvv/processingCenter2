package com.edme.processingCenter.mappers;

import com.edme.processingCenter.dto.MerchantCategoryCodeDto;
import com.edme.processingCenter.models.MerchantCategoryCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MerchantCategoryCodeMapper {
    MerchantCategoryCode toEntity(MerchantCategoryCodeDto merchantCategoryCodeDto);
    MerchantCategoryCodeDto toDto(MerchantCategoryCode merchantCategoryCode);
}
