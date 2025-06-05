package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.MerchantCategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MerchantCategoryCodeRepository extends JpaRepository<MerchantCategoryCode, Long> {

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.merchant_category_code CASCADE", nativeQuery = true)
    int dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.merchant_category_code (" +
                   "id bigserial primary key," +
                   "mcc varchar(4) UNIQUE not null," +
                   "mcc_name varchar(255) not null)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.merchant_category_code (mcc, mcc_name) VALUES" +
                   "('5309', 'Беспошлинные магазины Duty Free')," +
                   "('5651', 'Одежда для всей семьи')," +
                   "('5691', 'Магазины мужской и женской одежды')," +
                   "('5812', 'Места общественного питания, рестораны')," +
                   "('5814', 'Фастфуд') ON CONFLICT (mcc) DO NOTHING", nativeQuery = true)
    void insertDefaultValues();
}
