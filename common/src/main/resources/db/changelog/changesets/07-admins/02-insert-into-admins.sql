-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM admins where username='tansena54ang@gmail.com'
INSERT INTO `admins` (name, password, username, is_active, email, mobile_number, unique_id, created_at,status_id, access_group, wrong_password_attempt_count, two_factor_enabled, wrong_oto_auth_attempt_count, is_super_admin,version)
VALUES
    ('Tansen Angdembe', '$2a$10$h/Fm04H01xFqs1iZ8LEVPeg6YfEi/uRz1cLBI9i4KgoRKL0EHctsy', 'tansena54ang@gmail.com', true, 'tansena54ang@gmail.com', '9806008443', UUID(),now(), 1, 1, 0, false, 0, true,0);
