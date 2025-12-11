-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS municipality (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    version         BIGINT NOT NULL,
    govrenment_name VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    code            VARCHAR(255) NOT NULL UNIQUE,
    document_url    VARCHAR(255) NOT NULL,
    unique_id       VARCHAR(255) NOT NULL,
    province_id     BIGINT NOT NULL,
    district_id     BIGINT NOT NULL,
    local_level_id  BIGINT NOT NULL,
    latitude        VARCHAR(255) NOT NULL,
    longitude       VARCHAR(255) NOT NULL,
    address         VARCHAR(255) NOT NULL,
    created_at      DATETIME,
    updated_at      DATETIME,

    CONSTRAINT fk_municipality_provinces
    FOREIGN KEY (province_id) REFERENCES provinces(id),

    CONSTRAINT fk_municipality_districts
    FOREIGN KEY (district_id) REFERENCES districts(id),

    CONSTRAINT fk_municipality_local_levels
    FOREIGN KEY (local_level_id) REFERENCES local_levels(id)
    );
