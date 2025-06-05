package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionTypesRepository extends JpaRepository<TransactionType, Long> {

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.transaction_type CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.transaction_type(" +
                   "    id                    bigserial primary key," +
                   "    transaction_type_name varchar(255) UNIQUE not null," +
                   "    operator varchar(1) not null)", nativeQuery=true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value="INSERT INTO processingcenterschema.transaction_type(transaction_type_name, operator)" +
                 "VALUES ('Списание со счета', '-')," +
                 "       ('Пополнение счета', '+')", nativeQuery=true)
    void insertDefaultValues();
}
