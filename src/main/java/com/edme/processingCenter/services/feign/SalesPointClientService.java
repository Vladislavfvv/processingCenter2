package com.edme.processingCenter.services.feign;

import com.edme.common.exceptions.EmptyResponseException;
import com.edme.common.exceptions.ServerErrorException;
import com.edme.commondto.dto.TransactionExchangeDto;
import com.edme.processingCenter.client.SalesPointClient;
import feign.Retryer;
import io.opentelemetry.api.trace.Span;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalesPointClientService {
    private final SalesPointClient salesPointClient;
    private final RetryTemplate retryTemplate;
    private final SalesPointFallback salesPointFallback;


    //    public String confirmPaymentWithRetry(PaymentRequest request) {
//        return retryTemplate.execute(context -> {
//            String response = salesPointClient.confirmPayment(request);
//            if (response == null || response.isBlank()) {
//                throw new EmptyResponseException("Empty response from SalesPoint");
//            }
//            return response;
//        }, context -> {
//            throw new ServerErrorException("Retries exhausted when calling SalesPoint");
//        });
//    }


//    public String confirmPaymentWithRetry(TransactionExchangeDto dto) {
//        return retryTemplate.execute(context -> {
//            String response = salesPointClient.sendTransaction(dto);
//            if (response == null || response.isBlank()) {
//                throw new EmptyResponseException("Empty response from SalesPoint");
//            }
//            return response;
//        }, context -> {
//            log.info("Confirm payment with retry");
//            throw new ServerErrorException("Retries exhausted when calling SalesPoint");
//        });
//    }

//    public TransactionExchangeDto sendTransactionWithRetry(TransactionExchangeDto dto) {
//        return retryTemplate.execute(context -> {
//            TransactionExchangeDto response = salesPointClient.sendTransaction(dto);
//            if (response == null) {
//                throw new EmptyResponseException("Empty response from SalesPoint");
//            }
//            return response;
//        }, context -> {
//            log.error("Retries exhausted when calling SalesPoint");
//            throw new ServerErrorException("Retries exhausted when calling SalesPoint");
//        });
//    }

//    public TransactionExchangeDto sendTransactionWithRetry(TransactionExchangeDto dto) {
//        log.info("Attempting to send transaction to SalesPoint: {}", dto);
//        //RetryTemplate, который автоматизирует повторные попытки, вызывает основной блок
//        Span span = Span.current();
//        return retryTemplate.execute(
//                context -> {
//                    //подсчет попыток отправки
//                    int attempt = context.getRetryCount() + 1;
//                    log.info("Retry attempt #{} to send transaction to SalesPoint", attempt);
//
//                    try {
//                        //точка вызова Feign клиента, отправка запроса в sales-point посредством интерфейса подключения feign
//                        TransactionExchangeDto response = salesPointClient.sendTransaction(dto);
//                        if (response == null) { //если sales-point вернул null, считаем это ошибкой → выбрасываем исключение
//                            log.warn("Received null response on attempt #{}", attempt);
//                            throw new EmptyResponseException("Empty response from SalesPoint");
//                        }
//                        log.info("Transaction successfully sent to SalesPoint on attempt #{}", attempt);
//                        log.info("Transaction saved in SalesPoint with ID = {}, full object: {}", response.getId(), response);
//                        return response;//транзакция успешно отправлена, возвращается результат от sales-point.
//
//                    } catch (Exception e) {
//                        log.error("Error during attempt #{} to send transaction: {}", attempt, e.getMessage());
//                        throw e; // обязательно пробрасываем дальше, чтобы RetryTemplate сработал
//                    }
//                },
//                //при полном исчерпании попыток — вызывает fallback блок
//                context -> {
//                    Throwable lastThrowable = context.getLastThrowable();
//                    log.error("Retries exhausted when calling SalesPoint. Last exception: {}",
//                            lastThrowable != null ? lastThrowable.getMessage() : "unknown");
//
//                    throw new ServerErrorException("Retries exhausted when calling SalesPoint", lastThrowable);
//                }
//        );
//    }





    public TransactionExchangeDto sendTransactionWithRetry(TransactionExchangeDto dto) {
        log.info("Attempting to send transaction to SalesPoint: {}", dto);
        //RetryTemplate, который автоматизирует повторные попытки, вызывает основной блок
        Span span = Span.current();
        return retryTemplate.execute(
                context -> {
                    //подсчет попыток отправки
                    int attempt = context.getRetryCount() + 1;
                    log.info("Retry attempt #{} to send transaction to SalesPoint", attempt);

                    try {
                        //точка вызова Feign клиента, отправка запроса в sales-point посредством интерфейса подключения feign
                        TransactionExchangeDto response = salesPointClient.sendTransaction(dto);
                        if (response == null) { //если sales-point вернул null, считаем это ошибкой → выбрасываем исключение
                            log.warn("Received null response on attempt #{}", attempt);
                            throw new EmptyResponseException("Empty response from SalesPoint");
                        }
                        log.info("Transaction successfully sent to SalesPoint on attempt #{}", attempt);
                        log.info("Transaction saved in SalesPoint with ID = {}, full object: {}", response.getId(), response);
                        return response;//транзакция успешно отправлена, возвращается результат от sales-point.

                    } catch (Exception e) {
                        log.error("Error during attempt #{} to send transaction: {}", attempt, e.getMessage());
                        throw e; // обязательно пробрасываем дальше, чтобы RetryTemplate сработал
                    }
                },
                //при полном исчерпании попыток — вызывает fallback блок
                context -> {
                    Throwable lastThrowable = context.getLastThrowable();
                    log.error("Retries exhausted when calling SalesPoint. Last exception: {}",
                            lastThrowable != null ? lastThrowable.getMessage() : "unknown");

                    //throw new ServerErrorException("Retries exhausted when calling SalesPoint", lastThrowable);
                    // Вместо выбрасывания исключения, вернём null
                    return null;
                }
        );
    }





















//    //вариант без вылета исключения ServerErrorException
//    public TransactionExchangeDto sendTransactionWithRetry(TransactionExchangeDto dto) {
//        log.info("Attempting to send transaction to SalesPoint: {}", dto);
//
//        return retryTemplate.execute(
//                // ================================================
//                // основная логика попытки
//                // ================================================
//                context -> {
//                    int attempt = context.getRetryCount() + 1;
//                    log.info("Retry attempt #{} to send transaction to SalesPoint", attempt);
//                    TransactionExchangeDto response = salesPointClient.sendTransaction(dto);
//                    if (response == null) {
//                        log.warn("Received null response on attempt #{}", attempt);
//                        throw new EmptyResponseException("Empty response from SalesPoint");
//                    }
//                    log.info("Transaction successfully sent on attempt #{}", attempt);
//                    return response;
//                },
//                // ================================================
//                // recovery callback — срабатывает после исчерпания retries
//                // ================================================
//                context -> {
//                    Throwable last = context.getLastThrowable();
//                    log.warn("Retries exhausted after {} attempts, last error: {}. Applying fallback.",
//                            context.getRetryCount(), last.getMessage());
//                    // вместо броска, возвращаем результат из SalesPointFallback
//                    return salesPointFallback.sendTransaction(dto);
//                }
//        );
//    }
}

