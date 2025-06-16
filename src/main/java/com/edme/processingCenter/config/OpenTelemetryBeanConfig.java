package com.edme.processingCenter.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.time.Duration;
import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME;



import java.time.Duration;

@Configuration
public class OpenTelemetryBeanConfig {

    @Bean
    public OpenTelemetry openTelemetry() {
        // Jaeger exporter
        JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:14250")
                .setTimeout(Duration.ofSeconds(30))
                .build();

        // Tracer provider
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(jaegerExporter).build())
                .setResource(Resource.getDefault().toBuilder()
                        .put(SERVICE_NAME, "processing-center")
                        .build())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .buildAndRegisterGlobal(); // also makes GlobalOpenTelemetry.get() work
    }

    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("processing-center");
    }
}
