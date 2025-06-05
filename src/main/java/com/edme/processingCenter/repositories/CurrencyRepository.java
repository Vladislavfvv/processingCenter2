package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.currency CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.currency(" +
                   "    id                            bigserial primary key," +
                   "    currency_digital_code         varchar(3)          not null," +
                   "    currency_letter_code          varchar(3)          not null," +
                   "    currency_digital_code_account varchar(3)          not null," +
                   "    currency_name                 varchar(255) UNIQUE not null)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.currency(currency_digital_code, currency_letter_code, currency_digital_code_account," +
                   "                                       currency_name)" +
                   "VALUES ('643', 'RUB', '810', 'Russian Ruble')," +
                   " ('980', 'UAN', '980', 'Hryvnia')," +
                   " ('840', 'USD', '840', 'US Dollar')," +
                   " ('978', 'EUR', '978', 'Euro')," +
                   " ('392', 'JPY', '392', 'Yen')," +
                   " ('156', 'CNY', '156', 'Yuan Renminbi')," +
                   " ('826', 'GBP', '826', 'Pound Sterling')", nativeQuery = true)
    void insertDefaultValues();
}
