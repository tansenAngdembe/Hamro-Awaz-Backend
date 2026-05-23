-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
ALTER TABLE complaints
    MODIFY municipality_id BIGINT NULL;