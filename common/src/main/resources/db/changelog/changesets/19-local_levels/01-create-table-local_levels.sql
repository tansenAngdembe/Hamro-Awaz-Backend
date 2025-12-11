-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE
CREATE TABLE `local_levels`
(
    id              BIGINT    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    version            BIGINT NOT NULL DEFAULT 0,
    `local_level`      VARCHAR(70)     DEFAULT NULL,
    `local_level_code` INT             DEFAULT NULL,
    `total_wards`      INT             DEFAULT NULL,
    `district`         BIGINT             DEFAULT NULL,
     FOREIGN KEY (district) REFERENCES districts (id)
);
