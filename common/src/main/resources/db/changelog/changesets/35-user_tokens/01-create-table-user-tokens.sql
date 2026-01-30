-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE orError:HALT
CREATE TABLE IF NOT EXISTS user_tokens
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version       BIGINT                            NOT NULL,
    access_token  VARCHAR(255)                      NOT NULL,
    refresh_token VARCHAR(255)                      NOT NULL,
    logged_out    BOOLEAN                           NOT NULL DEFAULT FALSE,
    user_id       BIGINT                            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);