package com.edme.processingCenter.client;

//import com.edme.common.exceptions.*;
//import com.edme.processingCenter.dto.TransactionRequest;
//import com.edme.processingCenter.dto.TransactionResponse;
import com.edme.commondto.dto.TransactionExchangeDto;
import com.edme.processingCenter.config.FeignConfig;
import com.edme.processingCenter.services.feign.SalesPointFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//import com.edme.processingCenter.dto.feignDto.TransactionExchangeDto;


//Feign будет использовать именно FeignConfig для настройки поведения клиента SalesPointClient
@FeignClient(name = "sales-point",
        url = "${salespoint.url}",
        configuration = FeignConfig.class,
        fallback = SalesPointFallback.class
)
//@FeignClient(name = "sales-point", path = "/api/transactions")
public interface SalesPointClient {

@PostMapping("/api/transactions/external")
TransactionExchangeDto  sendTransaction(@RequestBody TransactionExchangeDto dto);

}