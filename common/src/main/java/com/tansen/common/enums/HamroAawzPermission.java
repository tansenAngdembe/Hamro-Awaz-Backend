package com.tansen.common.enums;

public enum HamroAawzPermission {
        // USER
        USER_DASHBOARD,
        CREATE_PROBLEM,
        VIEW_OWN_PROBLEMS,
        VIEW_ALL_PROBLEMS,
        EDIT_OWN_PROBLEM,
        DELETE_OWN_PROBLEM,
        VOTE_PROBLEM,
        COMMENT_PROBLEM,
        TRACK_PROBLEM_STATUS,
        VIEW_PROFILE,

        // AUTHORITY
        AUTHORITY,
        VERIFY_PROBLEM,
        ASSIGN_PROBLEM,
        UPDATE_PROBLEM_STATUS,
        RESOLVE_PROBLEM,
        REJECT_PROBLEM,
        ESCALATE_PROBLEM,
        MANAGE_TEAMS,
        VIEW_ANALYTICS,

        // ADMIN
        ADMIN_DASHBOARD,
        MANAGE_USERS,
        MANAGE_AUTHORITIES,
        MANAGE_ROLES,
        MANAGE_PERMISSIONS,
        VIEW_SYSTEM_LOGS,
        MANAGE_SLA,
        SYSTEM_SETTINGS
}
