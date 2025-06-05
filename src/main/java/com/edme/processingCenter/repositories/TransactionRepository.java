package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.transaction CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.transaction(" +
                   "    id                              bigserial primary key," +
                   "    transaction_date                date        not null," +
                   "    sum                             decimal," +
                   "    transaction_name                varchar(255) not null," +
                   "    account_id                      bigint REFERENCES processingcenterschema.account (id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    transaction_type_id             bigint REFERENCES processingcenterschema.transaction_type (id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    card_id bigint REFERENCES processingcenterschema.card (id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    terminal_id bigint REFERENCES processingcenterschema.terminal (id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    response_code_id bigint REFERENCES processingcenterschema.response_code (id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    authorization_code                varchar(6) not null," +
                   "    received_from_issuing_bank       timestamp," +
                   "    sent_to_issuing_bank timestamp)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.transaction(transaction_date, sum, transaction_name, account_id, " +
                   "transaction_type_id, card_id, terminal_id, response_code_id, authorization_code," +
                   "received_from_issuing_bank, sent_to_issuing_bank)" +
                   "VALUES ('2022-10-22', 1000.11, 'Cash deposit', 1, 2, 1, 1, 1, '123456',  '2022-10-22', '2022-10-22 09:05:23.129 ')," +
                   "       ('2022-04-06', 50000.92, 'Cash deposit', 2, 2, 2, 2, 2, '112233',  '2022-04-06', '2022-04-06 12:03:41.861 ')," +
                   "       ('2022-10-21', 750000.12, 'Cash deposit', 3, 2, 1, 1, 3, '546213',   '2022-10-21', '2022-10-21 12:03:41.861 ')," +
                   "       ('2022-10-23', 350.41, 'Money transfer', 1, 1, 2, 1, 3, '546211',  '2022-10-23', '2022-10-23 12:03:41.861 ')," +
                   "       ('2022-06-23', 1298.85, 'Commission', 2, 1, 1, 2, 1, '546222', '2022-06-23', '2022-06-23 12:03:41.861 ')," +
                   "       ('2022-10-22', 35000.11, 'Payment of the invoice', 3, 1, 2, 2, 3, '546233', '2022-10-22', '2022-10-22')," +
                   "       ('2022-10-22', 10000.0, 'Cash deposit', 4, 2, 1, 2, 2, '546244', '2022-10-22', '2022-10-22 12:03:41.861')", nativeQuery = true)
    void insertDefaultValues();
}
