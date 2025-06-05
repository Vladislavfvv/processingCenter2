package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.IssuingBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IssuingBankRepository extends JpaRepository<IssuingBank, Long> {

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.issuing_banks CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.issuing_bank" +
            "(\n" +
            "    id               bigserial primary key," +
            "    bic              varchar(9)   not null," +
            "    bin              varchar(5)   not null," +
            "    abbreviated_name varchar(255) not null)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.issuing_bank (bic, bin, abbreviated_name)" +
            "VALUES ('041234569', '12345', 'ОАО Приорбанк ')," +
            "       ('041234570', '45256', 'ОАО Сбербанк ')," +
            "       ('041234571', '98725', 'ЗАО МТБ Банк ')", nativeQuery = true)
    void insertDefaultValues();
}
