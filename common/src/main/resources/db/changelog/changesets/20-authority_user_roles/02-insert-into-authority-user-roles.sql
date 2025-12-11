-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) from authority_user_roles
INSERT INTO authority_user_roles (description, icon, `name`, navigation, parent_name, permission, position, ui_group_name, version)
VALUES
    ('Root', '', 'Root', 'NONE', 'ROOT', 'NONE', 0, 'NONE', 0),

    ('Staff', 'Users', 'Staff', '/staff', 'Root', 'STAFF', 1, 'NONE', 0),
    ('Create Staff', '', 'Create Staff', '/create', 'Staff', 'CREATE_STAFF', 1, 'Staff', 0),
    ('View Staff', '', 'View Staff', '/view', 'Staff', 'VIEW_STAFF', 2, 'Staff', 0),
    ('Edit Staff', '', 'Edit Staff', '/edit', 'Staff', 'EDIT_STAFF', 3, 'Staff', 0),
    ('Block Staff', '', 'Block Staff', '/block', 'Staff', 'BLOCK_STAFF', 4, 'Staff', 0),
    ('Unblock Staff', '', 'Unblock Staff', '/unblock', 'Staff', 'UNBLOCK_STAFF', 5, 'Staff', 0),
    ('Delete Staff', '', 'Delete Staff', '/delete', 'Staff', 'DELETE_STAFF', 6, 'Staff', 0),
    ('Send Staff Password Reset Link', '', 'Send Staff Password Reset Link', '/send-staff-password-reset-link', 'Staff',
     'SEND_STAFF_PASSWORD_RESET_LINK', 5, 'Staff', 0),
    ('Reset Staff Two Factor Authentication', '', 'Reset Staff Two Factor Authentication', '/reset-staff-2fa', 'Staff',
     'RESET_Staff_TWO_FACTOR_AUTHENTICATION', 6, 'Staff', 0),
    ('Resend Staff Account Activation Link', '', 'Resend Staff Account Activation Link',
     '/resend-staff-account-activation-link', 'Staff', 'RESEND_STAFF_ACCOUNT_ACTIVATION_LINK', 7, 'Staff', 0),

    ('Complaints', 'AlertTriangle', 'Complaints', '/complaints', 'Root', 'COMPLAINTS', 2, 'NONE', 0),
    ('View Complaints', '', 'View Complaints', '/view', 'Complaints', 'VIEW_COMPLAINTS', 1, 'Complaints', 0),
    ('Verify Complaint', '', 'Verify Complaint', '/verify', 'Complaints', 'VERIFY_COMPLAINT', 2, 'Complaints', 0),
    ('Assign Complaint', '', 'Assign Complaint', '/assign', 'Complaints', 'ASSIGN_COMPLAINT', 3, 'Complaints', 0),
    ('Update Status', '', 'Update Status', '/update-status', 'Complaints', 'UPDATE_COMPLAINT_STATUS', 4, 'Complaints', 0),
    ('Close Complaint', '', 'Close Complaint', '/close', 'Complaints', 'CLOSE_COMPLAINT', 5, 'Complaints', 0),
    ('Reject Complaint', '', 'Reject Complaint', '/reject', 'Complaints', 'REJECT_COMPLAINT', 6, 'Complaints', 0),
    ('Escalate Complaint', '', 'Escalate Complaint', '/escalate', 'Complaints', 'ESCALATE_COMPLAINT', 7, 'Complaints', 0),

    ('SLA Rules', 'Clock', 'SLA Rules', '/sla-rules', 'Root', 'SLA_RULES', 3, 'NONE', 0),
    ('Create SLA Rule', '', 'Create SLA Rule', '/create', 'SLA Rules', 'CREATE_SLA_RULE', 1, 'SLA Rules', 0),
    ('View SLA Rules', '', 'View SLA Rules', '/view', 'SLA Rules', 'VIEW_SLA_RULES', 2, 'SLA Rules', 0),
    ('Edit SLA Rule', '', 'Edit SLA Rule', '/edit', 'SLA Rules', 'EDIT_SLA_RULE', 3, 'SLA Rules', 0),
    ('Delete SLA Rule', '', 'Delete SLA Rule', '/delete', 'SLA Rules', 'DELETE_SLA_RULE', 4, 'SLA Rules', 0),
    ('Activate SLA Rule', '', 'Activate SLA Rule', '/activate', 'SLA Rules', 'ACTIVATE_SLA_RULE', 5, 'SLA Rules', 0),
    ('Deactivate SLA Rule', '', 'Deactivate SLA Rule', '/deactivate', 'SLA Rules', 'DEACTIVATE_SLA_RULE', 6, 'SLA Rules', 0),

    ('Map View', 'MapPin', 'Map View', '/map-view', 'Root', 'MAP_VIEW', 4, 'NONE', 0),
    ('View Map', '', 'View Map', '/view', 'Map View', 'VIEW_MAP', 1, 'Map View', 0),
    ('Filter by Status', '', 'Filter by Status', '/filter-status', 'Map View', 'FILTER_MAP_STATUS', 2, 'Map View', 0),
    ('Filter by Category', '', 'Filter by Category', '/filter-category', 'Map View', 'FILTER_MAP_CATEGORY', 3, 'Map View', 0),
    ('View Complaint Details', '', 'View Complaint Details', '/details', 'Map View', 'VIEW_MAP_COMPLAINT_DETAILS', 4, 'Map View', 0),

    ('Reports', 'BarChart', 'Reports', '/reports', 'Root', 'REPORTS', 5, 'NONE', 0),

    ('Settings', 'Settings', 'Settings', '/setting', 'Root', 'SETTING', 6, 'NONE', 0),
    ('Access Groups', 'Users', 'Access Groups', '/access-group', 'Settings',
     'ACCESS_GROUPS', 7, 'Settings', 0),

    ('System Configuration', 'MonitorCog', 'System Configuration', '/system-configuration', 'Settings',
     'SYSTEM_CONFIGURATION', 8, 'Settings', 0),
    ('Terms & Conditions', 'ReceiptText', 'Terms & Conditions', '/terms-and-conditions', 'Settings',
     'TERMS_&_CONDITIONS', 9, 'Settings', 0);