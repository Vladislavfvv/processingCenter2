package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.SalesPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SalesPointRepository extends JpaRepository<SalesPoint, Long> {

    Optional<SalesPoint> findByPosInn(String cardNumber);

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.sales_point CASCADE", nativeQuery = true)
    int dropTable();


    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.sales_point(" +
                   "    id                bigserial primary key," +
                   "    pos_name          varchar(255) not null," +
                   "    pos_address       varchar(255) not null," +
                   "    pos_inn           varchar(12) UNIQUE not null," +
                   "    acquiring_bank_id bigint REFERENCES processingcenterschema.acquiring_bank (id) ON DELETE CASCADE" +
                   "        ON UPDATE CASCADE\n" +
                   ")", nativeQuery = true)
    void createTable();


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.sales_point (pos_name," +
                   "                                                pos_address," +
                   "                                                pos_inn," +
                   "                                                acquiring_bank_id)" +
                   "VALUES ('Shop №1', 'City 1-st 1', '123456788888', 1)," +
                   "       ('Shop №2 ', 'City, 2-st 2 ', '159148777777', 3)," +
                   "       ('Shop №3', 'City, 3-st 3 ', '123596222222', 1)" +
                   "ON CONFLICT (pos_inn) DO NOTHING", nativeQuery = true)
    void insertDefaultValues();
}
