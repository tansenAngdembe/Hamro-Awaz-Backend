-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError: HALT
CREATE TABLE IF NOT EXISTS admin_tokens
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version       BIGINT                            NOT NULL,
    access_token  TEXT                              NOT NULL,
    refresh_token TEXT                              NOT NULL,
    logged_out    BOOLEAN                           NOT NULL DEFAULT FALSE,
    admin_id      BIGINT                            NOT NULL,
    FOREIGN KEY (admin_id) REFERENCES admins (id)
)