package com.edme.processingCenter.client;

//import com.edme.common.exceptions.*;
//import com.edme.processingCenter.dto.TransactionRequest;
//import com.edme.processingCenter.dto.TransactionResponse;
import com.edme.commondto.dto.TransactionExchangeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import com.edme.processingCenter.dto.feignDto.TransactionExchangeDto;

//@FeignClient(name = "sales-point", path = "/api/transactions")
@FeignClient(name = "sales-point", url = "${salespoint.url}")
//@FeignClient(name = "sales-point", path = "/api/transactions")
public interface SalesPointClient {

//    @PostMapping("/process")
//    ResponseEntity<TransactionExchangeDto> processTransaction(@RequestBody TransactionExchangeDto request)
//            throws ClientErrorException, ServerErrorException, EmptyResponseException;
    //@PostMapping("/api/transactions/confirm")
//@PostMapping("/api/transactions")
@PostMapping("/api/transactions/external")
//TransactionExchangeDto createTransaction(@RequestBody TransactionExchangeDto transaction);
TransactionExchangeDto  sendTransaction(@RequestBody TransactionExchangeDto dto);

}