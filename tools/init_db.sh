#!/bin/bash

psql -h database -U trainingapp -W trainingapp <<'__EOS__'
CREATE SEQUENCE IF NOT EXISTS t_member_seq AS BIGINT START WITH 1 INCREMENT BY 1 NO CYCLE; 

CREATE TABLE IF NOT EXISTS t_user (
    username        VARCHAR(255)  NOT NULL,
    password        VARCHAR(255)  NOT NULL,
    enabled         BOOLEAN       NOT NULL,
    PRIMARY KEY (userName)
);

CREATE TABLE IF NOT EXISTS t_member (
    member_id	    BIGINT NOT NULL,
    mail	        VARCHAR(255) NOT NULL,
    name	        VARCHAR(31) NOT NULL,
    address	        TEXT NOT NULL,
    start_date	    DATE NOT NULL,
    end_date	    DATE,
    payment_method	INT NOT NULL,
    created_at	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at	    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (member_id)
);

CREATE SEQUENCE IF NOT EXISTS t_charge_seq AS BIGINT START WITH 1 INCREMENT BY 1 NO CYCLE; 

CREATE TABLE IF NOT EXISTS t_charge (
    charge_id   BIGINT NOT NULL,
    name        VARCHAR(127) NOT NULL,
    amount      NUMERIC(9,0) NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (charge_id)
);

CREATE TABLE IF NOT EXISTS T_BILLING_STATUS (
    billing_ym      DATE PRIMARY KEY,
    is_commit       BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS T_BILLING_DATA (
    billing_ym      DATE NOT NULL,
    member_id       BIGINT NOT NULL,
    mail            VARCHAR(255) NOT nULL,
    name            VARCHAR(31) NOT NULL,
    address         VARCHAR(127) NOT nULL,
    start_date      DATE NOT NULL,
    end_date        DATE,    
    payment_method  INTEGER NOT NULL,
    amount          NUMERIC(10,0) NOT NULL,
    tax_ratio       NUMERIC(5,2) NOT NULL,
    total           NUMERIC(10,0) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id),
    FOREIGN KEY (billing_ym) 
        REFERENCES T_BILLING_STATUS(billing_ym)
);

CREATE TABLE IF NOT EXISTS T_BILLING_DETAIL_DATA (
    billing_ym      DATE NOT NULL,
    member_id       BIGINT NOT NULL,
    charge_id       BIGINT NOT NULL,
    name            VARCHAR(127) NOT NULL,
    amount          NUMERIC(9,0) NOT NULL,
    start_date      DATE NOT NULL,
    end_date        DATE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (billing_ym, member_id, charge_id),
    FOREIGN KEY (billing_ym, member_id) 
        REFERENCES T_BILLING_DATA(billing_ym, member_id)
);

BEGIN;

DELETE FROM T_MEMBER;
DELETE FROM T_USER;
DELETE FROM T_CHARGE;

INSERT INTO T_USER VALUES ('user', '$argon2id$v=19$m=14,t=2,p=1$eVczdXhrMWlDZERWUnZWdA$HjSDtkidFBp49L0k8ZlvtTVcKkC//uOkIjDRiYbGIWg', true);

INSERT INTO T_MEMBER VALUES (nextval('t_member_seq'), 'yamada@example.com', '山田　太郎', '東京都千代田区1-1-1', '2026-01-01', NULL, 1, NOW(), NOW());

INSERT INTO T_CHARGE VALUES (nextval('t_charge_seq'), 'シンプルパック', '990', '2026-01-01', NULL, NOW(), NOW());

COMMIT;
__EOS__