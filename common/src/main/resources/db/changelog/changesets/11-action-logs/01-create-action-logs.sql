-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError: HALT
CREATE TABLE IF NOT EXISTS action_logs(
                                          id          BIGINT AUTO_INCREMENT                               NOT NULL,
                                          version     BIGINT                                              NOT NULL,
                                          remarks     TEXT                                                NOT NULL,
                                          target_id   BIGINT                                              NOT NULL,
                                          target_type VARCHAR(50)                                         NOT NULL, -- e.g. 'ADMIN', 'PROPERTY'
    action_type ENUM ('CREATE','UPDATE','BLOCK','UNBLOCK','DELETE','SEND ADMIN PASSWORD RESET LINK', 'CHANGE PASSWORD', 'SET PASSWORD', 'RESET PASSWORD') NOT NULL,
    action_by   BIGINT                                              NOT NULL,
    action_date DATE                                                NOT NULL,
    ip_address  VARCHAR(255)                                        NOT NULL, -- stores IPv4/IPv6
    CONSTRAINT pk_action_log PRIMARY KEY (id),
    CONSTRAINT fk_action_by_admin FOREIGN KEY (action_by) REFERENCES admins (id)
)