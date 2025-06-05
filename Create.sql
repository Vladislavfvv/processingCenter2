
--
-- CREATE DATABASE processingCenter;
-- -- переключиться на БД
-- \c processingCenter;
-- CREATE SCHEMA IF NOT EXISTS processingCenterSchema;
-- -- установка схемы по умолчанию
-- SET search_path TO processingCenterSchema;


CREATE TABLE IF NOT EXISTS processingcenterschema.card_status
(
    id               bigserial primary key,
    card_status_name varchar(255) UNIQUE not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.payment_system
(
    id                  bigserial primary key,
    payment_system_name varchar(50) UNIQUE not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.currency
(
    id                    bigserial primary key,
    currency_digital_code varchar(3)   not null,
    currency_letter_code  varchar(3)   not null,
    currency_name         varchar(255) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.issuing_bank
(
    id               bigserial primary key,
    bic              varchar(9)   not null,
    bin              varchar(5)   not null,
    abbreviated_name varchar(255) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.acquiring_bank
(
    id               bigserial primary key,
    bic              varchar(9)   not null,
    abbreviated_name varchar(255) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.sales_point
(
    id                bigserial primary key,
    pos_name          varchar(255) not null,
    pos_address       varchar(255) not null,
    pos_inn           varchar(12)  not null,
    acquiring_bank_id bigint REFERENCES processingcenterschema.acquiring_bank (id) ON DELETE CASCADE
                                                            ON UPDATE CASCADE
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.merchant_category_code
(
    id       bigserial primary key,
    mcc      varchar(4) not null ,
    mcc_name varchar(255) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.terminal
(
    id          bigserial primary key,
    terminal_id varchar(9) not null ,
    mcc_id      bigint REFERENCES processingcenterschema.merchant_category_code (id) ON DELETE CASCADE
                                                              ON UPDATE CASCADE,
    pos_id      bigint REFERENCES processingcenterschema.sales_point (id) ON DELETE CASCADE
                                                              ON UPDATE CASCADE
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.response_code
(
    id                bigserial primary key,
    error_code        varchar(2) not null ,
    error_description varchar(255) not null ,
    error_level       varchar(255) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.transaction_type
(
    id                    bigserial primary key,
    transaction_type_name varchar(255) not null,
    operator              varchar(1) not null
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.account
(
    id              bigserial UNIQUE primary key,
    account_number  varchar(50) not null,
    balance         decimal,
    currency_id     bigint REFERENCES processingcenterschema.currency (id) ON DELETE CASCADE
                                                    ON UPDATE CASCADE,
    issuing_bank_id bigint REFERENCES processingcenterschema.issuing_bank (id) ON DELETE CASCADE
                                                    ON UPDATE CASCADE
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.card
(
    id                         bigserial primary key,
    card_number                varchar(50),
    expiration_date            date,
    holder_name                varchar(50),
    card_status_id             bigint REFERENCES processingcenterschema.card_status (id) ON DELETE CASCADE
                                                                  ON UPDATE CASCADE,
    payment_system_id          bigint REFERENCES processingcenterschema.payment_system (id) ON DELETE CASCADE
                                                                  ON UPDATE CASCADE,
    account_id                 bigint REFERENCES processingcenterschema.account ON DELETE CASCADE
                                                                  ON UPDATE CASCADE,
    received_from_issuing_bank timestamp,
    sent_to_issuing_bank       timestamp
    );
CREATE TABLE IF NOT EXISTS processingcenterschema.transaction
(
    id                         bigserial primary key,
    transaction_date           date,
    sum                        decimal,
    transaction_name           varchar(255),
    account_id                 bigint REFERENCES processingcenterschema.account ON DELETE CASCADE
                                                         ON UPDATE CASCADE,
    transaction_type_id        bigint REFERENCES processingcenterschema.transaction_type (id) ON DELETE CASCADE
                                                         ON UPDATE CASCADE,
    card_id                    bigint REFERENCES processingcenterschema.card (id) ON DELETE CASCADE
                                                         ON UPDATE CASCADE,
    terminal_id                bigint REFERENCES processingcenterschema.terminal ON DELETE CASCADE
                                                         ON UPDATE CASCADE,
    response_code_id           bigint REFERENCES processingcenterschema.response_code ON DELETE CASCADE
                                                         ON UPDATE CASCADE,
    authorization_code         varchar(6),
    received_from_issuing_bank timestamp,
    sent_to_issuing_bank       timestamp
    );


