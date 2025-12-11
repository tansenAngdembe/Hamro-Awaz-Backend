-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `authority_user_action_log`
(
    id          BIGINT AUTO_INCREMENT  PRIMARY KEY NOT NULL,
    version     BIGINT                                              NOT NULL,
    remarks     TEXT                                                NOT NULL,
    target_id   BIGINT                                              NOT NULL,
    target_type VARCHAR(50)                                         NOT NULL,
    action_type ENUM ('CREATE','UPDATE','BLOCK','UNBLOCK','DELETE','SEND ADMIN PASSWORD RESET LINK', 'CHANGE PASSWORD', 'SET PASSWORD', 'RESET PASSWORD') NOT NULL,
    action_by   BIGINT                                                  NOT NULL,
    action_date DATETIME                                                NOT NULL,
    ip_address  VARCHAR(255)                                        NOT NULL, -- stores IPv4/IPv6
    CONSTRAINT fk_authority_user_action_log_authority_users FOREIGN KEY (action_by) REFERENCES authority_users(id)
    );


