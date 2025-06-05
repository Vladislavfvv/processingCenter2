package com.edme.processingCenter.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class DelayForTestSwagger {
    public static void simulateSlowService() {
        long start = System.currentTimeMillis();
        log.info("Slow getting info from service. Start time: {}", start);
        try {
            Thread.sleep(5000L);
            long end = System.currentTimeMillis();
            log.info("Slow getting info from service. End time: {}", end);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // корректная обработка
            throw new IllegalStateException("Thread was interrupted", e);
        }
    }
}
