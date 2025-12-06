-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS access_groups_role_map
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    version       BIGINT                NOT NULL,
    access_groups BIGINT                NOT NULL,
    is_active     BIT(1)                NOT NULL,
    admin_roles   BIGINT                NOT NULL,
    CONSTRAINT pk_access_groups_role_map PRIMARY KEY (id),
    CONSTRAINT FK_ACCESS_GROUP_ROLE_MAP_ON_ACCESS_GROUP FOREIGN KEY (access_groups) REFERENCES access_groups (id),
    CONSTRAINT FK_ACCESS_GROUP_ROLE_MAP_ON_ROLES FOREIGN KEY (admin_roles) REFERENCES admin_roles (id)
);