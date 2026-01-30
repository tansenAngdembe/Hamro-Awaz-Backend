-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `user_registration_email_logs` (
    id               BIGINT AUTO_INCREMENT NOT NULL,
    version          BIGINT                NOT NULL,
    email            VARCHAR(255)          NOT NULL,
    `user`             BIGINT                NOT NULL,
    message          TEXT                  NOT NULL,
    is_sent          BOOLEAN,
    otp              VARCHAR(255)          NOT NULL,
    is_otp_expired   BOOLEAN DEFAULT FALSE,
    timestamp        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    unique_id        VARCHAR(255),
    expiration_time  TIMESTAMP,
    CONSTRAINT pk_user_email_log PRIMARY KEY (id)
    );
