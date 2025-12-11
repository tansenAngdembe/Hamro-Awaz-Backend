-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM authority_access_groups_role_map
INSERT INTO authority_access_groups_role_map (authority_access_groups_id, is_active, authority_user_roles_id,version)
SELECT (SELECT id from authority_access_groups WHERE name='Authority Admin'),
       true, id,0
from authority_user_roles;