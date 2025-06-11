package com.edme.processingCenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
//@EntityScan("com.edme.processingCenter.models")
////@EnableJpaRepositories("com.edme.processingCenter.repositories")
//@EnableJpaRepositories(basePackages = "com.edme.processingCenter.repositories")
@SpringBootApplication
//@ComponentScan(basePackages = "com.edme.processingCenter")
@EntityScan(basePackages = "com.edme.processingCenter.models")
@EnableJpaRepositories(basePackages = "com.edme.processingCenter.repositories")
@EnableCaching
//@EnableFeignClients
@EnableFeignClients(basePackages = "com.edme.processingCenter.client")
public class ProcessingCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProcessingCenterApplication.class, args);
	}
}
