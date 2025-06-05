package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.TransactionTypeDto;
import com.edme.processingCenter.models.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {
    @Mapping(target = "transactionTypeName", source = "transactionTypeName")
    TransactionTypeDto toDto(TransactionType transactionType);
    @Mapping(target = "transactionTypeName", source = "transactionTypeName")
    TransactionType toEntity(TransactionTypeDto transactionTypeDto);
}
