package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.AccountDto;
import com.edme.processingCenter.models.Account;
import com.edme.processingCenter.models.Currency;
import com.edme.processingCenter.models.IssuingBank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",//автоматическое создание бина т.к. есть spring и ручное как написано ниже не надо
        uses = {
                CurrencyMapper.class,
                IssuingBankMapper.class
        })
public interface AccountMapper {
//    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class); вручную создание бина

//    @Mapping(target = "currency", source = "currency")
//    @Mapping(target = "issuingBank", source = "issuingBank")
    AccountDto toDto(Account account);

//    @Mapping(target = "currency", source = "currency", qualifiedByName = "mapCurrency")
//    @Mapping(target = "issuingBank", source = "issuingBank", qualifiedByName = "mapIssuingBank")
    Account toEntity(AccountDto dto);

//    @Named("mapCurrency")
//    default Currency mapCurrency(Long id) {
//        if (id == null) return null;
//        Currency currency = new Currency();
//        currency.setId(id);
//        return currency;
//    }
//
//    @Named("mapIssuingBank")
//    default IssuingBank mapIssuingBank(Long id) {
//        if (id == null) return null;
//        IssuingBank issuingBank = new IssuingBank();
//        issuingBank.setId(id);
//        return issuingBank;
//    }

}
