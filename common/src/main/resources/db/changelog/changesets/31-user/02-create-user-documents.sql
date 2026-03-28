-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS `user_documents`
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    version BIGINT NOT NULL,
    citizenship_card_front VARCHAR(255) NOT NULL,
    citizenship_card_back VARCHAR(255) NOT NULL,
    national_identity_number VARCHAR(255) NOT NULL,

    municipality_id BIGINT NOT NULL,
    province_id BIGINT NOT NULL,
    district_id BIGINT NOT NULL,

    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    is_document_verified BOOLEAN NOT NULL,
    verification_status VARCHAR(20) NOT NULL,
    unique_id VARCHAR(100) NOT NULL,
    rejection_category VARCHAR(50) NOT NULL,
    rejection_reason TEXT NOT NULL,

    CONSTRAINT  fk_municipality_user_documents FOREIGN KEY (municipality_id) REFERENCES municipality(id),
    CONSTRAINT  fk_provinces_user_documents FOREIGN KEY (province_id) REFERENCES provinces(id),
    CONSTRAINT  fk_districts_user_documents FOREIGN KEY (district_id) REFERENCES districts(id),

    CONSTRAINT  fk_users_user_documents FOREIGN KEY (user_id) REFERENCES users(id)
    )