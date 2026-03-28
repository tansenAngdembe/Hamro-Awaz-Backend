package com.tansen.government.complaints.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.response.ListCommentResponse;

import java.security.Principal;

public interface CommentService {
    ApiResponse<?> listAllComment(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
}
