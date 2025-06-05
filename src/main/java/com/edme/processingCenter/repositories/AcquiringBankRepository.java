package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.AcquiringBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AcquiringBankRepository extends JpaRepository<AcquiringBank, Long> {

    Optional<AcquiringBank> findByBic(String bic);

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.acquiring_bank CASCADE", nativeQuery = true)
    int dropTable();


    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.acquiring_bank(" +
                   "    id               bigserial primary key," +
                   "    bic              varchar(9)   not null," +
                   "    abbreviated_name varchar(255) not null" +
                   "    )", nativeQuery = true)
    void createTable();


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.acquiring_bank (bic,\n" +
                   "                                                   abbreviated_name)\n" +
                   "VALUES ('041234567', 'ПАО Банк-эквайер №1'),\n" +
                   "       ('041234568', 'ПАО Банк-эквайер №2'),\n" +
                   "       ('041234569', 'ПАО Банк-эквайер №3') ON CONFLICT (bic) DO NOTHING", nativeQuery = true)
    void insertDefaultValues();
}
