package com.edme.processingCenter.repositories;


import com.edme.processingCenter.models.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {
    @Modifying
    @Transactional
    @Query(value="DROP TABLE IF EXISTS processingcenterschema.card_status CASCADE", nativeQuery=true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value="CREATE TABLE IF NOT EXISTS processingcenterschema.card_status(" +
                 "    id               bigserial primary key," +
                 "    card_status_name varchar(255) UNIQUE not null)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value="INSERT INTO processingcenterschema.card_status(card_status_name)" +
                 "VALUES ('Card is not active')," +
                 "       ('Card is valid')," +
                 "       ('Card is temporarily blocked')," +
                 "       ('Card is lost')," +
                 "       ('Card is compromised')", nativeQuery=true)
    void insertDefaultValues();


}
