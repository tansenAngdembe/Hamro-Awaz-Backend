-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE IF NOT EXISTS categories (
     id  BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     version  BIGINT NOT NULL DEFAULT 0,
     category_name VARCHAR(255) NOT NULL,
     description VARCHAR(255)  NOT NULL,
     unique_id VARCHAR(255) NOT NULL,
     create_at  DATETIME,
     update_at DATETIME,
     municipality_id BIGINT NOT NULL,

    CONSTRAINT fk_categories_municipality
    FOREIGN KEY (municipality_id) REFERENCES municipality(id)
);