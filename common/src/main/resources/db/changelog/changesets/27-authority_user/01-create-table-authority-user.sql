-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS authority_users (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                 version BIGINT NOT NULL,

                                 municipality_id BIGINT NOT NULL,
                                 `name` VARCHAR(255) NOT NULL,
                                 email VARCHAR(255) NOT NULL UNIQUE,
                                 password VARCHAR(255),
                                 status_id BIGINT NOT NULL,
                                 created_at DATETIME,
                                 updated_at DATETIME,
                                 password_changed_date DATETIME,
                                 last_logged_in_time DATETIME,

                                 wrong_password_attempt_count INT,
                                 profile_picture_name VARCHAR(255),
                                 otp_auth_secret VARCHAR(255),

                                 two_factor_enabled BOOLEAN NOT NULL,
                                 wrong_oto_auth_attempt_count INT,
                                 is_authority_admin BOOLEAN NOT NULL,

                                 authority_access_group_id BIGINT NOT NULL,

                                 CONSTRAINT fk_authority_users_municipality  FOREIGN KEY (municipality_id) REFERENCES municipality(id),
                                 CONSTRAINT fk_authority_users_status FOREIGN KEY (status_id) REFERENCES status(id),
                                 CONSTRAINT fk_authority_users_authority_access_groups FOREIGN KEY (authority_access_group_id) REFERENCES authority_access_groups(id)
);
