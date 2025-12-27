-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM roles
INSERT INTO roles (description,name, version) VALUES
                                              ( 'System Administrator','ROLE_ADMIN',0),
                                              ('Municipality / Authority User','ROLE_AUTHORITY', 0),
                                              ( 'User of HamroAwaz','ROLE_USER', 0);