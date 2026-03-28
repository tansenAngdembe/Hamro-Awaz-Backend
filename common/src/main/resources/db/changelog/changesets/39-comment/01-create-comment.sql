-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS comments(
    id  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version BIGINT  NOT NULL,
    message TEXT NOT NULL,
    comment_by      BIGINT NOT NULL,
    complaint_id     BIGINT NOT NULL,
    unqiue_id  VARCHAR(255) NOT NULL,
    comment_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_ip VARCHAR(50),
    updated_at TIMESTAMP,
    is_delete BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_comments_users FOREIGN KEY (comment_by) REFERENCES users(id),
    CONSTRAINT fk_comments_complaints FOREIGN KEY (complaint_id) REFERENCES complaints(id)

    );