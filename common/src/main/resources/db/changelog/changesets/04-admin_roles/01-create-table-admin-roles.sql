-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS admin_roles
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    version       BIGINT                NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    `description` VARCHAR(255)          NULL,
    icon          VARCHAR(255)          NULL,
    navigation    VARCHAR(255)          NULL,
    position      INT                   NULL,
    ui_group_name VARCHAR(255)          NULL,
    parent_role   BIGINT                NULL,
    parent_name   VARCHAR(255)          NULL,
    permission    VARCHAR(255)          NULL,
    CONSTRAINT pk_admin_roles PRIMARY KEY (id),
    CONSTRAINT FK_ADMIN_ROLES_ON_PARENT_ROLE FOREIGN KEY (parent_role) REFERENCES admin_roles (id)
    );
