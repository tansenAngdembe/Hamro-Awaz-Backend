-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM access_groups_role_map
INSERT INTO access_groups_role_map (access_groups, is_active, admin_roles,version)
SELECT (SELECT id from access_groups WHERE name='Super Admin'),
       true, id,0
        from admin_roles;