-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT

CREATE TABLE IF NOT EXISTS forgot_password_otp (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    version BIGINT,
    email VARCHAR(255),
    otp INT NOT NULL,
    user_unique_id VARCHAR(255),
    expiration_time TIMESTAMP NOT NULL,
    is_valid BOOLEAN,
    created_at TIMESTAMP
);
