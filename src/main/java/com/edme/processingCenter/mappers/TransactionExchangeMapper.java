package com.edme.processingCenter.mappers;

import com.edme.commondto.dto.TransactionExchangeDto;
import com.edme.processingCenter.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionExchangeMapper {

    @Mapping(source = "transactionType.id", target = "transactionTypeId")
    @Mapping(source = "card.id", target = "cardId")
    @Mapping(source = "terminal.id", target = "terminalId")
    @Mapping(source = "responseCode.id", target = "responseCodeId")
    TransactionExchangeDto toExchangeDto(TransactionDto dto);
}
