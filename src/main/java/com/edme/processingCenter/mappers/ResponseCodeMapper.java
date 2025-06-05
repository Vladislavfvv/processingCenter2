package com.edme.processingCenter.mappers;


import com.edme.processingCenter.dto.ResponseCodeDto;
import com.edme.processingCenter.models.ResponseCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseCodeMapper {
    ResponseCodeDto toDto(ResponseCode responseCode);
    ResponseCode toEntity(ResponseCodeDto responseCode);
}
