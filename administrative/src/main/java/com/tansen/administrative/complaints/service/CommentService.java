package com.tansen.administrative.complaints.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;

import java.security.Principal;

public interface CommentService {
    ApiResponse<?> listAllComment(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
}
