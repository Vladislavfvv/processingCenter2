package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import com.edme.processingCenter.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        TransactionTypeMapper.class,
        AccountMapper.class
})
public interface TransactionExchangeIbMapper {
    @Mapping(source = "transactionType", target = "transactionType.id")
    @Mapping(source = "account", target = "account.id")
    Transaction toEntity(TransactionExchangeIbDto dto);

}
