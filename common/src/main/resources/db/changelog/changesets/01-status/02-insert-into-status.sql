-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM status

INSERT INTO status (description, color, name, version)
VALUES
    ('ACTIVE', '#14A44D', 'ACTIVE',0),
    ('DELETED', '#DC4C64', 'DELETED',0),
    ('PENDING', '#E4A11B', 'PENDING',0),
    ('BLOCKED', '#3B71CA', 'BLOCKED',0);

