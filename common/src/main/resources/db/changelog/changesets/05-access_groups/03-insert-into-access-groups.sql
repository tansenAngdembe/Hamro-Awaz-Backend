-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
INSERT INTO access_groups (name, description, created_at, status, is_super_admin_group, remarks,version)
VALUES
    ('Super Admin', 'System group with full access', now(), 1, true, 'Super admin group',0);

