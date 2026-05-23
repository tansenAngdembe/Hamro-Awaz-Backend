package com.tansen.admin.complaints.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.complaints.dto.ComplaintUniqueDto;
import com.tansen.admin.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.admin.complaints.dto.response.ComplaintResponse;
import com.tansen.admin.complaints.dto.response.ListComplainsResponse;
import com.tansen.admin.complaints.mapper.ComplaintMapper;
import com.tansen.admin.complaints.service.ComplaintService;
import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.dto.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.common.utility.redisutil.RedisHelper;
import com.tansen.entity.*;
import com.tansen.repository.*;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import com.tansen.repository.searchrepo.ComplaintSearchRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintServiceImpl.class);

    private final ComplaintRepository complaintRepository;
    private final AuthorityUserRepository authorityUserRepository;
    private final ComplaintSearchRepository complaintSearchRepository;
    private final ComplaintMapper complaintMapper;
    private final SearchResponse searchResponse;
    private final StatusRepository statusRepository;
    private final ComplainStatusRepository complainStatusRepository;

    private final AuthorityUserSearchRepository authorityUserSearchRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MailService mailService;
    private final ActionLogMapper actionLogMapper;
    private final AdminRepository adminRepository;
    private final ServletRequest httpServletRequest;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, AuthorityUserRepository authorityUserRepository, ComplaintSearchRepository complaintSearchRepository, ComplaintMapper complaintMapper, SearchResponse searchResponse, StatusRepository statusRepository, ComplainStatusRepository complainStatusRepository, AuthorityUserSearchRepository authorityUserSearchRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, MailService mailService, ActionLogMapper actionLogMapper, AdminRepository adminRepository, ServletRequest httpServletRequest) {
        this.complaintRepository = complaintRepository;
        this.authorityUserRepository = authorityUserRepository;
        this.complaintSearchRepository = complaintSearchRepository;
        this.complaintMapper = complaintMapper;
        this.searchResponse = searchResponse;
        this.statusRepository = statusRepository;
        this.complainStatusRepository = complainStatusRepository;
        this.authorityUserSearchRepository = authorityUserSearchRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.mailService = mailService;
        this.actionLogMapper = actionLogMapper;
        this.adminRepository = adminRepository;
        this.httpServletRequest = httpServletRequest;
    }



    @Override
    public ApiResponse<?> listComplains(SearchParam searchParam) {
        SearchResponseWithMapperBuilder<Complaint, ListComplainsResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<Complaint, ListComplainsResponse>builder()
                        .count(complaintSearchRepository::count)       // no municipalityId
                        .searchData(complaintSearchRepository::getAll) // no municipalityId
                        .mapperFunction(this.complaintMapper::listComplainsResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListComplainsResponse> response =
                searchResponse.getSearchResponse(responseBuilder);

        LOG.info("All complaints listed successfully by super admin");
        return ResponseUtil.getSuccessfulApiResponse(response, "Complaints listed successfully");
    }

    @Override
    public ApiResponse<?> getComplaint(ComplaintUniqueDto complaintUniqueIdDto, Principal loggedInAdmin) {
        Admin authorityUserOpt =
                adminRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt == null) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in Admin Not Found.");
        }

        Complaint complaint = complaintRepository
                .findByUniqueId(complaintUniqueIdDto.getUniqueId());
        if( complaint == null ) {
            return ResponseUtil.getFailureResponse("Complaint not found.");
        }

        ComplaintResponse complaintResponse = complaintMapper.entityToComplaintResponse(complaint);
        return ResponseUtil.getSuccessfulApiResponse(complaintResponse, "Complaint fetched by Id");
    }

// ─────────────────────────────────────────────
// RESOLVE
// ─────────────────────────────────────────────

    @Override
    public ApiResponse<?> resolveComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest)  {

        Admin admin = getSuperAdmin(loggedInAdmin);
        if (admin == null) return ResponseUtil.getFailureResponse("Logged in User Not Found.");

        Complaint complaint = getComplaint(complaintUniqueIdDto.getComplaintUniqueId());

        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint is already rejected. It cannot be resolved.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint is already resolved.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.CLOSED.getName())) {
            return ResponseUtil.getFailureResponse("Closed complaint cannot be resolved.");
        }

        complaint.setResolvedAt(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.RESOLVED.getName()));

        saveAndInvalidateCache(complaint);
        actionLogMapper.resolveComplaint(complaint.getId(), loggedInAdmin,httpServletRequest);

        return ResponseUtil.getSuccessfulApiResponse("Complaint updated to RESOLVED status.");
    }

// ─────────────────────────────────────────────
// REJECT
// ─────────────────────────────────────────────

    @Override
    public ApiResponse<?> rejectComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin) {

        Admin admin = getSuperAdmin(loggedInAdmin);
        if (admin == null) return ResponseUtil.getFailureResponse("Logged in User Not Found.");

        Complaint complaint = getComplaint(complaintUniqueIdDto.getComplaintUniqueId());

        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint is already rejected.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Resolved complaint cannot be rejected.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.CLOSED.getName())) {
            return ResponseUtil.getFailureResponse("Closed complaint cannot be rejected.");
        }

        complaint.setUpdatedDate(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.REJECTED.getName()));

        saveAndInvalidateCache(complaint);

        return ResponseUtil.getSuccessfulApiResponse("Complaint updated to REJECTED status.");
    }

// ─────────────────────────────────────────────
// CLOSE
// ─────────────────────────────────────────────

    @Override
    public ApiResponse<?> closedComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest) {

        Admin admin = getSuperAdmin(loggedInAdmin);
        if (admin == null) return ResponseUtil.getFailureResponse("Logged in User Not Found.");

        Complaint complaint = getComplaint(complaintUniqueIdDto.getComplaintUniqueId());

        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint rejected. It cannot be closed.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint resolved. It cannot be closed.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.CLOSED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint is already closed.");
        }

        complaint.setActive(false);
        complaint.setUpdatedDate(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.CLOSED.getName()));

        saveAndInvalidateCache(complaint);

        return ResponseUtil.getSuccessfulApiResponse("Complaint closed successfully.");
    }

// ─────────────────────────────────────────────
// IN PROGRESS
// ─────────────────────────────────────────────

    @Override
    public ApiResponse<?> inProgressComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin) {

        Admin admin = getSuperAdmin(loggedInAdmin);
        if (admin == null) return ResponseUtil.getFailureResponse("Logged in User Not Found.");

        Complaint complaint = getComplaint(complaintUniqueIdDto.getComplaintUniqueId());

        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Rejected complaint cannot be moved to In Progress.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Resolved complaint cannot be moved to In Progress.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.CLOSED.getName())) {
            return ResponseUtil.getFailureResponse("Closed complaint cannot be moved to In Progress.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.IN_PROGRESS.getName())) {
            return ResponseUtil.getFailureResponse("Complaint is already In Progress.");
        }

        complaint.setUpdatedDate(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.IN_PROGRESS.getName()));

        saveAndInvalidateCache(complaint);

        return ResponseUtil.getSuccessfulApiResponse("Complaint updated to IN PROGRESS status.");
    }

// ─────────────────────────────────────────────
// PRIVATE HELPERS
// ─────────────────────────────────────────────

    /**
     * Saves the complaint and invalidates ALL municipality complaint caches,
     * since super admin operates globally across all municipalities.
     */
    private void saveAndInvalidateCache(Complaint complaint) {
        complaintRepository.save(complaint);
        invalidateAllComplaintCaches();
    }

    /**
     * Deletes all Redis complaint cache entries across all municipalities.
     * Uses SCAN with a global complaint pattern to avoid blocking Redis.
     */
    private void invalidateAllComplaintCaches() {
        String pattern = RedisHelper.buildGlobalCacheKeyPattern(); // e.g. "complaint:*"
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
        List<String> keys = new ArrayList<>();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            cursor.forEachRemaining(keys::add);
        }

        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
            LOG.info("Super admin invalidated {} Redis complaint cache entries globally", keys.size());
        } else {
            LOG.info("No Redis complaint cache entries found to invalidate.");
        }
    }

    /**
     * Fetches the super admin by email from Principal.
     * Returns null if not found (caller should return failure response).
     */
    private Admin getSuperAdmin(Principal loggedInAdmin) {
        Admin admin = adminRepository.findByEmail(loggedInAdmin.getName());
        if (admin == null) {
            LOG.error("Failed to find super admin by email {}", loggedInAdmin.getName());
            return null;
        }
        return admin;
    }

    /**
     * Fetches complaint by uniqueId globally (no municipality scoping).
     * Throws RuntimeException if not found.
     */
    private Complaint getComplaint(String uniqueId) {
        Complaint complaint =  complaintRepository
                .findByUniqueId(uniqueId);
        if(complaint == null ){
             throw new RuntimeException("Complaint not found");
        }
        return complaint;
    }



}
