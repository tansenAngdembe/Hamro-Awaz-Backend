-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS complaints (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                            version BIGINT NOT NULL,

                            unique_id VARCHAR(100) NOT NULL,
                            complaint_title VARCHAR(255) NOT NULL,
                            complaint_description TEXT NOT NULL,

                            category_id BIGINT NOT NULL,
                            status_id BIGINT NOT NULL,
                            reported_by BIGINT NOT NULL,
                            municipality_id BIGINT NOT NULL,
                            assigned_to BIGINT NULL,

                            resolved_at DATETIME NULL,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            priority VARCHAR(50) NOT NULL,

                            photo_url VARCHAR(500) NOT NULL,

                            created_date DATETIME NOT NULL,
                            updated_date DATETIME,
                            escalated_at DATETIME,
                            escalation_id BIGINT,

                            CONSTRAINT uq_complaints_unique_id UNIQUE (unique_id),

                            CONSTRAINT fk_complaints_categories
                                FOREIGN KEY (category_id) REFERENCES categories(id),

                            CONSTRAINT fk_complaints_status
                                FOREIGN KEY (status_id) REFERENCES complaint_status(id),

                            CONSTRAINT fk_complaints_reported_by
                                FOREIGN KEY (reported_by) REFERENCES users(id),

                            CONSTRAINT fk_complaints_municipality
                                FOREIGN KEY (municipality_id) REFERENCES municipality(id),

                            CONSTRAINT fk_complaints_assigned_to
                                FOREIGN KEY (assigned_to) REFERENCES authority_users(id),
                            CONSTRAINT fk_complaints_escalations FOREIGN KEY (escalation_id) REFERENCES escalations(id)
);
