--liquibase formatted sql
--changeset tansen:1
--preconditions onFail:CONTINUE onError:HALT
INSERT INTO complaint_status (description, color, name, version)
VALUES
    ('New complaint registered', '#0D6EFD', 'NEW', 0),                -- Blue
    ('Complaint is under review', '#FFC107', 'IN_REVIEW', 0),        -- Yellow
    ('Complaint assigned to authority', '#0DCAF0', 'ASSIGNED', 0),    -- Cyan
    ('Work in progress', '#6610F2', 'IN_PROGRESS', 0),                -- Purple
    ('Issue successfully resolved', '#14A44D', 'RESOLVED', 0),        -- Green
    ('Complaint escalated to higher authority', '#DC3545', 'ESCALATED', 0), -- Red
    ('Complaint rejected as invalid', '#6C757D', 'REJECTED', 0),      -- Grey
    ('Complaint closed', '#198754', 'CLOSED', 0);                     -- Dark Green
