-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE IF NOT EXISTS departments (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    version  BIGINT NOT NULL,
    department_name VARCHAR(225) NOT NULL,
    description TEXT NOT NULL,
    municipality_id BIGINT NOT NULL,

    CONSTRAINT fk_departments_municipality FOREIGN KEY (municipality_id) REFERENCES municipality(id)
);