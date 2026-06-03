-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
ALTER TABLE `user_documents`
    MODIFY rejection_category VARCHAR(50) NULL;

-- changeset tansen:2
ALTER TABLE `user_documents`
    MODIFY rejection_reason TEXT NULL;