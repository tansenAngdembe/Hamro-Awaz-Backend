-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE districts
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    version       BIGINT NOT NULL DEFAULT 0,
    district_name VARCHAR(70),
    province_id   BIGINT,
    FOREIGN KEY (province_id) REFERENCES provinces (id)
);

