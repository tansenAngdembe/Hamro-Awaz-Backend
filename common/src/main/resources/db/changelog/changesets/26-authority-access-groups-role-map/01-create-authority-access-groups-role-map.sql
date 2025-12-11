-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS authority_access_groups_role_map (
    id    BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version  BIGINT NOT NULL,
    authority_access_groups_id BIGINT NOT NULL,
    authority_user_roles_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL,

    CONSTRAINT fk_aagrm_access_groups
    FOREIGN KEY (authority_access_groups_id)
    REFERENCES authority_access_groups(id),

    CONSTRAINT fk_aagrm_user_roles
    FOREIGN KEY (authority_user_roles_id)
    REFERENCES authority_user_roles(id)
    );

