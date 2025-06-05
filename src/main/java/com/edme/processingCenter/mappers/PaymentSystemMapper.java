package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.PaymentSystemDto;
import com.edme.processingCenter.models.PaymentSystem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {
    PaymentSystem toEntity(PaymentSystemDto paymentSystemDto);
    PaymentSystemDto toDto(PaymentSystem paymentSystem);
}
