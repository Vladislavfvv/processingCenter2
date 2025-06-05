package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.PaymentSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {

    @Modifying
    @Transactional
    @Query(value="DROP TABLE IF EXISTS processingcenterschema.payment_system CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value="CREATE TABLE IF NOT EXISTS processingcenterschema.payment_system(" +
                 "    id                  bigserial primary key," +
                 "    payment_system_name varchar(50) UNIQUE not null)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value="INSERT INTO processingcenterschema.payment_system(payment_system_name)" +
                 "VALUES ('VISA International Service Association')," +
                 "       ('Mastercard')," +
                 "       ('JCB')," +
                 "       ('American Express')," +
                 "       ('Diners Club International')," +
                 "       ('China UnionPay')", nativeQuery = true)
    void insertDefaultValues();

}
