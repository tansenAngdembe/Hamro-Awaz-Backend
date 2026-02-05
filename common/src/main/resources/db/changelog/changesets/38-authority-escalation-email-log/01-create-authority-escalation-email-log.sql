-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS authority_escalation_email_logs (
                 id  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                 version BIGINT  NOT NULL,
                 complaint_title VARCHAR(255) NOT NULL,
                 category VARCHAR(255)  NOT NULL,
                 created_date   TIMESTAMP  NOT NULL,
                 complaint_rule VARCHAR(255) NOT NULL,
                 assigned_to      BIGINT NOT NULL,
                 escalation_at  TIMESTAMP NOT NULL,
                 message TEXT NOT NULL,
                 unqiue_id  VARCHAR(255) NOT NULL,
                 meta_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

                CONSTRAINT fk_authority_escalation_email_logs_authority_users FOREIGN KEY (assigned_to) REFERENCES authority_users(id)
    );