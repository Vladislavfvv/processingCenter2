package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.SalesPointDto;
import com.edme.processingCenter.models.SalesPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        AcquiringBankMapper.class
})
public interface SalesPointMapper {
//    @Mapping(source = "acquiringBankId", target = "acquiringBank.id")
//    SalesPoint toEntity(SalesPointDto salesPointDto);
//
//    @Mapping(source = "acquiringBank.id", target = "acquiringBankId")
//    SalesPointDto toDto(SalesPoint salesPoint);

    @Mapping(source = "acquiringBank", target = "acquiringBank")
    SalesPointDto toDto(SalesPoint salesPoint);

    @Mapping(source = "acquiringBank.id", target = "acquiringBank.id")
    SalesPoint toEntity(SalesPointDto salesPointDto);

    default SalesPoint mapSalesPoint(Long id) {
        if (id == null) return null;
        SalesPoint salesPoint = new SalesPoint();
        salesPoint.setId(id);
        return salesPoint;
    }
}
