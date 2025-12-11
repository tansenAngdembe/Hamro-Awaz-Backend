-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError: HALT
CREATE TABLE IF NOT EXISTS authority_user_tokens(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version             BIGINT                   NOT NULL,
    access_token        TEXT                     NOT NULL,
    refresh_token       TEXT                     NOT NULL,
    logged_out          BOOLEAN                  NOT NULL DEFAULT FALSE,
    authority_user_id   BIGINT                      NOT NULL,
    CONSTRAINT fk_authority_user_tokens_authority_users FOREIGN KEY (authority_user_id) REFERENCES authority_users(id)
);