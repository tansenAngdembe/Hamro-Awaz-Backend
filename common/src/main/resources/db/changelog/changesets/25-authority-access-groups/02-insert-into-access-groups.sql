-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
INSERT INTO authority_access_groups (name, description, created_at, status, is_authority_admin_group, remarks,version)
VALUES
    ('Authority Admin', 'System group with full access', now(), 1, true, 'Authority admin group',0);

