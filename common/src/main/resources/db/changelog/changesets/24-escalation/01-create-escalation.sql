-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE IF NOT EXISTS escalations (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                             version BIGINT NOT NULL,
                             max_resolution_hours INT NOT NULL,
                             escalation_level INT NOT NULL,
                             active BOOLEAN NOT NULL,
                             municipality_id BIGINT NOT NULL,

                             CONSTRAINT fk_escalations_municipality
                                 FOREIGN KEY (municipality_id)
                                     REFERENCES municipality(id)
);
