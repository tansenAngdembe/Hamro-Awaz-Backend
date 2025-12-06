-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS access_groups (
    id BIGINT AUTO_INCREMENT NOT NULL,
    version BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL,
    status BIGINT NOT NULL,
    is_super_admin_group BIT(1) NOT NULL,
    remarks VARCHAR(255) NULL,
    CONSTRAINT pk_access_group PRIMARY KEY (id),
    CONSTRAINT FK_ACCESS_GROUP_ON_STATUS FOREIGN KEY (status) REFERENCES status (id)
    );
