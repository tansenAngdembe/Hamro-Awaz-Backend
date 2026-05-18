-- liquibase formatted sql
-- changeset tansen:1
-- preconditions onFail:CONTINUE onError:HALT
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) from admin_roles
INSERT INTO admin_roles (description, icon, `name`, navigation, parent_name, permission, position, ui_group_name, version)
VALUES
    ('Root', '', 'Root', 'NONE', 'ROOT', 'NONE', 0, 'NONE', 0),

    ('System Administrator', 'UserCog ', 'System Administrator', '/admin', 'Root', 'SYSTEM_ADMINISTRATOR', 1, 'NONE', 0),
    ('Create System Administrator', '', 'Create System Administrator', '/create', 'System Administrator', 'CREATE_SYSTEM_ADMINISTRATOR', 1, 'System Administrator', 0),
    ('View System Administrator', '', 'View System Administrator', '/view', 'System Administrator', 'VIEW_SYSTEM_ADMINISTRATOR', 2, 'System Administrator', 0),
    ('Edit System Administrator', '', 'Edit System Administrator', '/edit', 'System Administrator', 'EDIT_SYSTEM_ADMINISTRATOR', 3, 'System Administrator', 0),
    ('Delete System Administrator', '', 'Delete System Administrator', '/delete', 'System Administrator', 'DELETE_SYSTEM_ADMINISTRATOR', 4, 'System Administrator', 0),
    ('Block System Administrator', '', 'Block System Administrator', '/block', 'System Administrator', 'BLOCK_SYSTEM_ADMINISTRATOR', 5, 'System Administrator', 0),
    ('Unblock System Administrator', '', 'Unblock System Administrator', '/unblock', 'System Administrator', 'UNBLOCK_SYSTEM_ADMINISTRATOR', 6, 'System Administrator', 0),
    ('Resend System Administrator Account Activation Link', '', 'Resend System Administrator Account Activation Link',
     '/resend-system-administrator-account-activation-link', 'System Administrator', 'RESEND_SYSTEM_ADMINISTRATOR_ACCOUNT_ACTIVATION_LINK', 7, 'System Administrator', 0),
    ('Send System Administrator Password Reset Link', '', 'Send System Administrator Password Reset Link',
     '/send-system-administrator-password-reset-link',
     'System Administrator', 'SEND_SYSTEM_ADMINISTRATOR_PASSWORD_RESET_LINK', 8, 'System Administrator', 0),
    ('Reset System Administrator Two Factor Authentication', '', 'Reset System Administrator Two Factor Authentication',
     '/reset-system-administrator-2fa',
     'System Administrator', 'RESET_SYSTEM_ADMINISTRATOR_TWO_FACTOR_AUTHENTICATION', 9, 'System Administrator', 0),

    ('Administrative Units', 'Building2', 'Administrative', '/administrative', 'Root', 'ADMINISTRATIVE', 2, 'NONE', 0),
    ('View All Administrative', '', 'View All Administrative', '/view', 'Administrative', 'VIEW_ALL_ADMINISTRATIVE', 1, 'Administrative', 0),
    ('Create Administrative', '', 'Create Administrative', '/create', 'Administrative', 'CREATE_ADMINISTRATIVE', 2, 'Administrative', 0),
    ('Edit Administrative', '', 'Edit Administrative', '/edit', 'Administrative', 'EDIT_ADMINISTRATIVE', 3, 'Administrative', 0),
    ('Block Administrative', '', 'Block Administrative', '/block', 'Administrative', 'BLOCK_ADMINISTRATIVE', 4, 'Administrative', 0),
    ('Unblock Administrative', '', 'Unblock Administrative', '/unblock-administrative', 'Administrative', 'UNBLOCK_ADMINISTRATIVE', 5, 'Administrative', 0),
    ('Delete Administrative', '', 'Delete Administrative', '/delete-administrative', 'Administrative', 'DELETE_ADMINISTRATIVE', 6, 'Administrative', 0),
    ('Send Administrative Password Reset Link', '', 'Send Administrative Password Reset Link', '/send-administrative-password-reset-link', 'Administrative',
     'SEND_ADMINISTRATIVE_PASSWORD_RESET_LINK', 7, 'Administrative', 0),
    ('Reset Administrative Two Factor Authentication', '', 'Reset Administrative Two Factor Authentication', '/reset-administrative-2fa', 'Administrative',
     'RESET_ADMINISTRATIVE_TWO_FACTOR_AUTHENTICATION', 8, 'Administrative', 0),
    ('Resend Administrative Account Activation Link', '', 'Resend Administrative Account Activation Link',
     '/resend-administrative-account-activation-link', 'Administrative', 'RESEND_ADMINISTRATIVE_ACCOUNT_ACTIVATION_LINK', 9, 'Administrative', 0),


    ('Users', 'Users', 'Users', '/users', 'Root', 'USERS', 3, 'NONE', 0),
    ('View User', '', 'View User', '/view', 'Users', 'VIEW_USER', 1, 'Users', 0),
    ('Edit User', '', 'Edit User', '/edit', 'Users', 'EDIT_USER', 2, 'Users', 0),
    ('Block User', '', 'Block User', '/block', 'Users', 'BLOCK_USER', 3, 'Users', 0),
    ('Unblock User', '', 'Unblock User', '/unblock-user', 'Users', 'UNBLOCK_USER', 4, 'Users', 0),
    ('Delete User', '', 'Delete User', '/delete-user', 'Users', 'DELETE_USER', 5, 'Users', 0),
    ('Send User Password Reset Link', '', 'Send User Password Reset Link', '/send-user-password-reset-link', 'Users',
     'SEND_USER_PASSWORD_RESET_LINK', 5, 'Users', 0),
    ('Reset User Two Factor Authentication', '', 'Reset User Two Factor Authentication', '/reset-user-2fa', 'Users',
     'RESET_USER_TWO_FACTOR_AUTHENTICATION', 6, 'Users', 0),
    ('Resend User Account Activation Link', '', 'Resend User Account Activation Link',
     '/resend-user-account-activation-link', 'Users', 'RESEND_USER_ACCOUNT_ACTIVATION_LINK', 7, 'Users', 0),

    ('Complaints', 'AlertTriangle', 'Complaints', '/complaints', 'Root', 'COMPLAINTS', 4, 'NONE', 0),
    ('View Complaints', '', 'View Complaints', '/view', 'Complaints', 'VIEW_COMPLAINTS', 1, 'Complaints', 0),
    ('Verify Complaint', '', 'Verify Complaint', '/verify', 'Complaints', 'VERIFY_COMPLAINT', 2, 'Complaints', 0),
    ('Assign Complaint', '', 'Assign Complaint', '/assign', 'Complaints', 'ASSIGN_COMPLAINT', 3, 'Complaints', 0),
    ('Update Status', '', 'Update Status', '/update-status', 'Complaints', 'UPDATE_COMPLAINT_STATUS', 4, 'Complaints', 0),
    ('Close Complaint', '', 'Close Complaint', '/close', 'Complaints', 'CLOSE_COMPLAINT', 5, 'Complaints', 0),
    ('Reject Complaint', '', 'Reject Complaint', '/reject', 'Complaints', 'REJECT_COMPLAINT', 6, 'Complaints', 0),
    ('Escalate Complaint', '', 'Escalate Complaint', '/escalate', 'Complaints', 'ESCALATE_COMPLAINT', 7, 'Complaints', 0),


    -- Comments & Voting
    ('Interactions', 'MessageSquare', 'Interactions', '/interactions', 'Root', 'INTERACTIONS', 5, 'None', 0),
    ('Comment on Complaint', '', 'Comment', '/comment', 'Interactions', 'COMMENT_COMPLAINT', 1, 'INTERACTIONS', 0),
    ('Vote Complaint', '', 'Vote', '/vote', 'Interactions', 'VOTE_COMPLAINT', 2, 'INTERACTIONS', 0),

    ('Reports', 'BarChart', 'Reports', '/reports', 'Root', 'REPORTS', 6, 'None', 0),
    ('View Reports', '', 'View Reports', '/view', 'Reports', 'VIEW_REPORTS', 1, 'REPORTS', 0),
    ('Export Reports', '', 'Export Reports', '/export', 'Reports', 'EXPORT_REPORTS', 2, 'REPORTS', 0),


    ('App', 'LayoutDashboard', 'App', '/app', 'Root', 'APP', 6, 'NONE', 0),

    ('Settings', 'Settings', 'Settings', '/setting', 'Root', 'SETTING', 6, 'NONE', 0),
    ('Access Groups', 'Users', 'Access Groups', '/access-group', 'Settings',
     'ACCESS_GROUPS', 1, 'Settings', 0),
    ('Tax & Service Charges', 'TicketPercent', 'Tax & Service Charges', '/tax-and-service-charges', 'Settings',
     'TAX_&_SERVICE_CHARGES', 2, 'Settings', 0),
    ('System Configuration', 'MonitorCog', 'System Configuration', '/system-configuration', 'Settings',
     'SYSTEM_CONFIGURATION', 3, 'Settings', 0),
    ('Terms & Conditions', 'ReceiptText', 'Terms & Conditions', '/terms-and-conditions', 'Settings',
     'TERMS_&_CONDITIONS', 4, 'Settings', 0),
    ('Email Templates', 'Mails', 'Email Templates', '/email-templates', 'Settings',
     'EMAIL_TEMPLATE', 5, 'Settings', 0),
    ('View Email Template', '', 'View Email Template', '/view', 'Email Templates',
     'VIEW_EMAIL_TEMPLATE', 1, 'Email Templates', 0),
    ('Modify Email Template', '', 'Modify Email Template', '/edit', 'Email Templates',
     'MODIFY_EMAIL_TEMPLATE', 2, 'Email Templates', 0),
    ('SMS Templates', 'MessagesSquare', 'SMS Templates', '/sms-templates', 'Settings',
     'SMS_TEMPLATE', 6, 'Settings', 0),
    ('View SMS Template', '', 'View SMS Template', '/view', 'SMS Templates',
     'VIEW_SMS_TEMPLATE', 1, 'SMS Templates', 0),
    ('Modify SMS Template', '', 'Modify SMS Template', '/edit', 'SMS Templates',
     'MODIFY_SMS_TEMPLATE', 2, 'SMS Templates', 0);