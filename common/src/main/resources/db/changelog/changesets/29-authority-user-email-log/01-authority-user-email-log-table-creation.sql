-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `authority_user_email_logs`(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version      BIGINT                NOT NULL,
    email        VARCHAR(255)          NOT NULL,
    authority_user  BIGINT       NULL,
    message      TEXT                  NOT NULL,
    is_sent      BOOLEAN,
    is_expired   BOOLEAN DEFAULT FALSE,
    unique_id         VARCHAR(255)          NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_authority_user_email_logs_authority_users
    FOREIGN KEY (authority_user) REFERENCES authority_users(id)
    ON DELETE SET NULL ON UPDATE CASCADE
    );

DROP EVENT IF EXISTS update_authority_email_log_is_expired;

CREATE EVENT IF NOT EXISTS update_authority_email_log_is_expired
ON SCHEDULE EVERY 1 MINUTE
DO UPDATE authority_user_email_logs
   SET is_expired = TRUE
   WHERE created_at <= NOW() - INTERVAL 24 HOUR
     AND is_expired = FALSE;

SET GLOBAL event_scheduler = ON;
