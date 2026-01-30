-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `user_email_logs`
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    version     BIGINT                NOT NULL,
    email       VARCHAR(255)          NOT NULL,
    user        BIGINT                NOT NULL,
    message     TEXT                  NOT NULL,
    is_sent     BOOLEAN,
    is_expired  BOOLEAN DEFAULT FALSE,
    uuid        VARCHAR(255)          NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_user_email_logs PRIMARY KEY (id)
    );

-- changeset tansen:2
-- preconditions onFail:CONTINUE onError:HALT
CREATE EVENT IF NOT EXISTS update_is_expired
    ON SCHEDULE EVERY 1 MINUTE
    DO
BEGIN
UPDATE user_email_logs
SET is_expired = TRUE
WHERE created_at <= NOW() - INTERVAL 24 HOUR AND is_expired = FALSE;
END;

-- changeset tensen:3
-- preconditions onFail:CONTINUE onError:HALT
SET GLOBAL event_scheduler = ON;
