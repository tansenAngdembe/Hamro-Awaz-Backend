-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS votes (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                     version BIGINT NOT NULL,
                                     unique_id VARCHAR(255) NOT NULL,

                       complaint_id BIGINT NOT NULL,
                       voted_by BIGINT NOT NULL,

                       remarks TEXT,
    voted_at DATETIME NOT NULL,
    synced_from_redis BOOLEAN,
                       CONSTRAINT uq_votes_complaint_voted_by
                           UNIQUE (complaint_id, voted_by),

                       CONSTRAINT fk_votes_complaint
                           FOREIGN KEY (complaint_id)
                               REFERENCES complaints(id)
                               ON DELETE CASCADE,

                       CONSTRAINT fk_votes_voted_by
                           FOREIGN KEY (voted_by)
                               REFERENCES users(id)
);
