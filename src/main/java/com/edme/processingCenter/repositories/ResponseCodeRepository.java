package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.ResponseCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResponseCodeRepository extends JpaRepository<ResponseCode, Long> {
    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.response_code CASCADE", nativeQuery = true)
    int dropTable();


    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.response_code (" +
                   "id                bigserial primary key," +
                   "error_code        varchar(2) UNIQUE not null," +
                   "error_description varchar(255) not null," +
                   "error_level       varchar(255) not null" +
                   ")", nativeQuery = true)
    void createTable();


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.response_code (error_code, error_description, error_level) VALUES " +
                   "('00', 'одобрено и завершено', 'Все в порядке')," +
                   "('01', 'авторизация отклонена, обратиться в банк-эмитент', 'не критическая')," +
                   "('03', 'незарегистрированная торговая точка или агрегатор платежей', 'не критическая')," +
                   "('05', 'авторизация отклонена, оплату не проводить', 'критическая')," +
                   "('41', 'карта утеряна, изъять', 'критическая')," +
                   "('51', 'недостаточно средств на счёте', 'сервисная или аппаратная ошибка')," +
                   "('55', 'неправильный PIN', 'не критическая') ON CONFLICT (error_code) DO NOTHING", nativeQuery = true)
    void insertDefaultValues();
}
