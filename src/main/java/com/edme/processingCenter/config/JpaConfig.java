package com.edme.processingCenter.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class JpaConfig {
    @Bean //Все методы с @Bean будут создавать Spring Beans — объекты, управляемые контейнером Spring
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) { //Создает и настраивает EntityManagerFactory, который нужен для работы JPA
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();//Адаптер для работы Hibernate как JPA-провайдера
        vendorAdapter.setGenerateDdl(true);//Генерировать SQL таблицы автоматически на основе Entity-классов (если база позволяет).
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        //Фабрика для создания EntityManagerFactory, которая управляет JPA Entity-менеджерами.
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.edme.processingCenter.models"); // Папка, где искать Entity-классы (@Entity)
        factory.setDataSource(dataSource);//Устанавливается источник данных (DataSource), который предоставляет соединения с базой данных
        return factory;
    }
    //Создает JpaTransactionManager, который управляет транзакциями базы данных (например, чтобы изменения были атомарными)
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);//Связываем его с EntityManagerFactory, чтобы он знал, с кем работать
        return txManager;
    }
}
