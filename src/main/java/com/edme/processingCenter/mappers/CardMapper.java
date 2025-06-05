package com.edme.processingCenter.mappers;

import com.edme.processingCenter.dto.CardDto;
import com.edme.processingCenter.dto.PaymentSystemDto;
import com.edme.processingCenter.models.Account;
import com.edme.processingCenter.models.Card;
import com.edme.processingCenter.models.CardStatus;
import com.edme.processingCenter.models.PaymentSystem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
        CardStatusMapper.class,
        PaymentSystemMapper.class,
        AccountMapper.class
})
public interface CardMapper {
    // CardMapper INSTANCE = Mappers.getMapper(CardMapper.class); //нужно только при ручном маппинге
//
//    @Mapping(target = "cardStatus", source = "cardStatus")
//    @Mapping(target = "paymentSystem", source = "paymentSystem")
//    @Mapping(target = "account", source = "account")
    CardDto toDto(Card card);


//    @Mapping(target = "cardStatus", source = "cardStatus", qualifiedByName = "mapCardStatus")
//    @Mapping(target = "paymentSystem", source = "paymentSystem", qualifiedByName = "mapPaymentSystem")
//    @Mapping(target = "account", source = "account", qualifiedByName = "mapAccount")
    Card toEntity(CardDto dto);

//    @Named("mapCardStatus")
//    default CardStatus mapCardStatus(Long id) {
//        if (id == null) {
//            return null;
//        }
//        CardStatus cardStatus = new CardStatus();
//        cardStatus.setId(id);
//        return cardStatus;
//    }
//
//    @Named("mapPaymentSystem")
//    default PaymentSystem mapPaymentSystem(PaymentSystemDto dto) {
//        if (dto == null) {
//            return null;
//        }
//        PaymentSystem paymentSystem = new PaymentSystem();
//        paymentSystem.setId(dto.getId());
//        return paymentSystem;
//    }
//
//    @Named("mapAccount")
//    default Account mapAccount(Long id) {
//        if (id == null) return null;
//        Account account = new Account();
//        account.setId(id);
//        return account;
//    }

}
