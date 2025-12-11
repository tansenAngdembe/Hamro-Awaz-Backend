-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `provinces`
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    version  BIGINT NOT NULL DEFAULT 0,
    province VARCHAR(100)
);