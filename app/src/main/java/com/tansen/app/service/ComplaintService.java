package com.tansen.app.service;

import com.tansen.app.dto.request.CreateComplaintRequest;
import com.tansen.app.dto.request.UpdateComplaintRequest;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface ComplaintService {
    ApiResponse<?> createComplaint(CreateComplaintRequest createComplaint, MultipartFile photos, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException;
    ApiResponse<?> updateComplaint(UpdateComplaintRequest updateComplaintRequest,MultipartFile photos, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException;

}
