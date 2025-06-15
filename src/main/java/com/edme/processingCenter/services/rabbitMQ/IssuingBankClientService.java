package com.edme.processingCenter.services.rabbitMQ;

import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.opentelemetry.api.trace.Span;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Setter
@Service
public class IssuingBankClientService {
    @Value("${queue.name}")
    private String queueName;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private Tracer tracer;
    //    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ObjectMapper objectMapper;

    //    public void sendTransactionWithRabbitMq(TransactionExchangeIbDto transactionExchangeIbDto) {
//        amqpTemplate.convertAndSend(queueName, transactionExchangeIbDto);
//    }
//    public void sendTransactionWithRabbitMq(TransactionExchangeIbDto transactionExchangeIbDto) {
//        // Создаем span
//        Span span = tracer.spanBuilder("sendTransactionWithRabbitMq")
//                .setAttribute("queue.name", queueName)
//                .startSpan();
//
//        // Оборачиваем в контекст
//        try (Scope scope = span.makeCurrent()) {
//            // Можно добавить атрибуты

    /// /        span.setAttribute("transaction.id", transactionExchangeIbDto.getId());
//            if (transactionExchangeIbDto.getId() != null) {
//                span.setAttribute("transaction.id", transactionExchangeIbDto.getId());
//            } else {
//                span.setAttribute("transaction.id", "null");
//            }
//            span.setAttribute("transaction.amount", String.valueOf(transactionExchangeIbDto.getSum()));
//
//            // Отправка сообщения
//            amqpTemplate.convertAndSend(queueName, transactionExchangeIbDto);
//
//            // Событие внутри спана
//            span.addEvent("Transaction sent to RabbitMQ");
//        } catch (Exception ex) {
//            span.recordException(ex);
//            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, "Error sending message");
//            throw ex;
//        } finally {
//            span.end();
//        }
//    }
    public void sendTransactionWithRabbitMq(TransactionExchangeIbDto transactionExchangeIbDto) {
        Span span = tracer.spanBuilder("sendTransactionWithRabbitMq")
                .setAttribute("queue.name", queueName)
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            // Добавим атрибуты
            span.setAttribute("transaction.id", String.valueOf(transactionExchangeIbDto.getId() != null ? transactionExchangeIbDto.getId() : "null"));
            span.setAttribute("transaction.amount", String.valueOf(transactionExchangeIbDto.getSum()));

            // Подготовим заголовки с trace context
            MessageProperties messageProperties = new MessageProperties();
            TextMapPropagator propagator = GlobalOpenTelemetry.getPropagators().getTextMapPropagator();

            propagator.inject(Context.current(), messageProperties, new TextMapSetter<MessageProperties>() {
                @Override
                public void set(MessageProperties carrier, String key, String value) {
                    if (carrier != null) {
                        carrier.setHeader(key, value);
                    }
                }
            });

//            // Сериализуем тело
//            byte[] body = objectMapper.writeValueAsBytes(transactionExchangeIbDto);
//            mapper.registerModule(new JavaTimeModule());
//            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            Message message = new Message(body, messageProperties);
//
//            // Отправим
//            amqpTemplate.send(queueName, message);
//            span.addEvent("Transaction sent to RabbitMQ");
//        } catch (Exception ex) {
//            span.recordException(ex);
//            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, "Error sending message");
//            throw new RuntimeException("RabbitMQ sending failed", ex);
//        } finally {
//            span.end();
//        }
            // Конфигурация objectMapper
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            byte[] body = objectMapper.writeValueAsBytes(transactionExchangeIbDto);
            Message message = new Message(body, messageProperties);

            amqpTemplate.send(queueName, message);
            span.addEvent("Transaction sent to RabbitMQ");
        } catch (Exception ex) {
            span.recordException(ex);
            span.setStatus(io.opentelemetry.api.trace.StatusCode.ERROR, "Error sending message");
            throw new RuntimeException("RabbitMQ sending failed", ex);
        } finally {
            span.end();
        }
    }
}
