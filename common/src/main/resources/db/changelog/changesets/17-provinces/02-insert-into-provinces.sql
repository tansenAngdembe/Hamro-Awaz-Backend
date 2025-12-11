-- liquibase-formatted-sql
-- changeset tansen:1
-- preconditions onError: HALT onFail: CONTINUE

INSERT INTO provinces (province)
VALUES ('Koshi'),
       ('Madhesh'),
       ('Bagmati'),
       ('Gandaki'),
       ('Lumbini'),
       ('Karnali'),
       ('Sudurpaschim');
