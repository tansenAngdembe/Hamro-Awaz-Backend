-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `users` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version BIGINT NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    unique_id VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255),
    password VARCHAR(255),
    address VARCHAR(255),

    is_active BOOLEAN,
    account_status BIGINT NOT NULL,
    `role` BIGINT NOT NULL,

    registered_date DATETIME,
    created_at DATETIME,
    updated_at DATETIME,
    password_changed_date DATETIME,
    last_logged_in_time DATETIME,

    wrong_password_attempt_count INT,
    profile_picture_link VARCHAR(255),
    municipality_id BIGINT,
    is_user_verified BOOLEAN NOT NULL,


    CONSTRAINT  fk_users_roles FOREIGN KEY (`role`) REFERENCES roles(id),
    CONSTRAINT  fk_users_municipality FOREIGN KEY (municipality_id) REFERENCES municipality(id),
    CONSTRAINT fk_users_status FOREIGN KEY (account_status) REFERENCES status(id)


    )
