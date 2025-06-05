package com.edme.processingCenter.repositories;

import com.edme.processingCenter.models.Account;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Transactional
    @Query(value = "DROP TABLE IF EXISTS processingcenterschema.account CASCADE", nativeQuery = true)
    void dropTable();

    @Modifying
    @Transactional
    @Query(value = "CREATE TABLE IF NOT EXISTS processingcenterschema.account(" +
                   "    id bigserial primary key," +
                   "    account_number varchar(20) UNIQUE not null," +
                   "    balance decimal," +
                   "    currency bigint REFERENCES processingcenterschema.currency(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                   "    issuing_bank bigint REFERENCES processingcenterschema.issuing_bank (id) ON DELETE CASCADE\n" +
                   "                                                    ON UPDATE CASCADE)", nativeQuery = true)
    void createTable();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO processingcenterschema.account (account_number," +
                   "                                            balance," +
                   "                                            currency_id," +
                   "                                            issuing_bank_id)" +
                   "VALUES ('40817810800000000001', 649.7, 1, 1)," +
                   "       ('40817810100000000002', 48702.07, 1, 1)," +
                   "       ('40817810400000000003', 715000.01, 1, 1)," +
                   "       ('40817810400000000003', 10000.0, 3, 1) ON CONFLICT (account_number) DO NOTHING", nativeQuery = true)
    void insertDefaultValues();


    Optional<Account> findByAccountNumber(@NotNull(message = "Account number is required") @Pattern(regexp = "^\\d{20}$", message = "Account number must be exactly 20 digits") String accountNumber);
}


