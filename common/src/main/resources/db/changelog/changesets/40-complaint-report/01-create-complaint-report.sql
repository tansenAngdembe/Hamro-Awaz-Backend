-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS complaint_report (
                                                id                         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                                version                    BIGINT NOT NULL,

                                                from_date                  DATE NOT NULL,
                                                to_date                    DATE NOT NULL,

                                                administrative_id          BIGINT,

                                                total_complaints           BIGINT,
                                                resolved                   BIGINT,
                                                pending                    BIGINT,
                                                in_progress                BIGINT,
                                                escalated                  BIGINT,

                                                total_change_percent       DOUBLE,
                                                resolved_change_percent    DOUBLE,
                                                in_progress_change_percent DOUBLE,
                                                escalated_change_percent   DOUBLE,

                                                total_comments             BIGINT,
                                                total_votes                BIGINT,
                                                upvotes                    BIGINT,
                                                downvotes                  BIGINT,

                                                sla_breached_count         BIGINT,
                                                escalation_rate            DOUBLE,

                                                top_category_id            BIGINT,
                                                top_category_count         BIGINT,
                                                most_active_user_id        BIGINT,

                                                generated_at               DATETIME NOT NULL,
                                                CONSTRAINT fk_complaint_report_municipality
                                                FOREIGN KEY (administrative_id) REFERENCES municipality(id)
    );