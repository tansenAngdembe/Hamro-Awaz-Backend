-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS complain_status
(
    id            BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    version       BIGINT                NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` VARCHAR(255)          NOT NULL,
    color          VARCHAR(255)          NULL
    )
