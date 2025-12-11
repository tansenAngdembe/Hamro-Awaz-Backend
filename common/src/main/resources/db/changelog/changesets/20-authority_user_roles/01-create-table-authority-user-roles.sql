-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS authority_user_roles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version         BIGINT                NOT NULL,
    name            VARCHAR(255)       NOT NULL,
    `description`   VARCHAR(255)       NULL,
    icon            VARCHAR(255)       NULL,
    navigation      VARCHAR(255)       NULL,
    position        INT                NULL,
    ui_group_name   VARCHAR(255)       NULL,
    parent_role     BIGINT                NULL,
    parent_name     VARCHAR(255)       NULL,
    permission      VARCHAR(255)       NULL,
    CONSTRAINT FK_AUTHORITY_USER_ROLES_ON_PARENT_ROLE FOREIGN KEY (parent_role) REFERENCES authority_user_roles(id)
    );
