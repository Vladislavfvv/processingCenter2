
DROP TABLE IF EXISTS processingcenterschema.transaction;
DROP TABLE IF EXISTS processingcenterschema.card;
DROP TABLE IF EXISTS processingcenterschema.account;
DROP TABLE IF EXISTS processingcenterschema.terminal;
DROP TABLE IF EXISTS processingcenterschema.card_status;
DROP TABLE IF EXISTS processingcenterschema.payment_system;
DROP TABLE IF EXISTS processingcenterschema.currency;
DROP TABLE IF EXISTS processingcenterschema.issuing_bank;
DROP TABLE IF EXISTS processingcenterschema.sales_point;
DROP TABLE IF EXISTS processingcenterschema.acquiring_bank;
DROP TABLE IF EXISTS processingcenterschema.response_code;
DROP TABLE IF EXISTS processingcenterschema.transaction_type;
DROP TABLE IF EXISTS processingcenterschema.merchant_category_code;



DROP SCHEMA IF EXISTS processingcenterschema;




SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'processingCenter'
  AND pid <> pg_backend_pid();


DROP DATABASE IF EXISTS processingCenter;