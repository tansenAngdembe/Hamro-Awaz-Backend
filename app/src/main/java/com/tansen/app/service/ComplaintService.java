package com.tansen.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.NearByComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;

public interface ComplaintService {
    ApiResponse<?> createComplaint(CreateComplaintRequest createComplaint, Principal loggedUser, HttpServletRequest httpServletRequest, MultipartFile photos ) throws IOException;
    ApiResponse<?> updateComplaint(UpdateComplaintRequest updateComplaintRequest,MultipartFile photos, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException;
    ApiResponse<?> listNearByComplains(
            NearByComplaintRequest nearByComplaintRequest
    ) throws JsonProcessingException;
    ApiResponse<?> listMyComplaints(SearchParam searchParam, Principal loggedInUser);
}
