-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE IF NOT EXISTS escalations (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                             version BIGINT NOT NULL,
                             rule_name VARCHAR(255) NOT NULL,
                             max_resolution_time INT NOT NULL,
                             escalation_time INT NOT NULL,
                             response_time INT,
                             active BOOLEAN NOT NULL,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP,
                             category_id BIGINT,
                             municipality_id BIGINT NOT NULL,

                             CONSTRAINT fk_escalations_municipality
                                 FOREIGN KEY (municipality_id)
                                     REFERENCES municipality(id),
                             CONSTRAINT fk_escalations_categories FOREIGN KEY (category_id) REFERENCES categories(id)
);
