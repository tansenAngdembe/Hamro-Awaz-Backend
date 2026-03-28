-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
CREATE TABLE IF NOT EXISTS complaint_coordinates (
         id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
         version BIGINT NOT NULL,
         latitude  DOUBLE(10,7) NOT NULL,
         longitude DOUBLE(10,7) NOT NULL,
         complaint_id BIGINT NOT NULL,

         CONSTRAINT fk_complaints_coordinates_complaints
         FOREIGN KEY (complaint_id) REFERENCES complaints(id)

    );
