package com.tansen.government.complaints.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.dto.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.government.actionlog.mapper.ActionLogMapper;
import com.tansen.government.complaints.dto.ComplaintUniqueDto;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import com.tansen.government.complaints.mapper.ComplaintMapper;
import com.tansen.government.complaints.service.ComplaintService;
import com.tansen.government.municipality.dto.AssignToListResponse;
import com.tansen.government.util.redisutil.RedisHelper;
import com.tansen.repository.*;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import com.tansen.repository.searchrepo.ComplaintSearchRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
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

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, AuthorityUserRepository authorityUserRepository, ComplaintSearchRepository complaintSearchRepository, ComplaintMapper complaintMapper, SearchResponse searchResponse, StatusRepository statusRepository, ComplainStatusRepository complainStatusRepository, AuthorityUserSearchRepository authorityUserSearchRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, MailService mailService, ActionLogMapper actionLogMapper) {
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
    }

    @Override
    public ApiResponse<?> listComplains(SearchParam searchParam, Principal loggedInAdmin) throws JsonProcessingException {

        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        Municipality municipality = authorityUser.getMunicipality();

        if (municipality == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any municipality.");
        }

        Long municipalityId = municipality.getId();
        // REDIS CACHE KEY
        String cacheKey = RedisHelper.buildCacheKey(municipalityId, searchParam);

        String cachedJson =
                (String) redisTemplate.opsForValue().get(cacheKey);

        if (cachedJson != null) {
            PageableResponse<ListComplainsResponse> cached =
                    objectMapper.readValue(
                            cachedJson,
                            new TypeReference<PageableResponse<ListComplainsResponse>>() {}
                    );
        LOG.info("Complaints fetched from redis for key {}", cacheKey);

            return ResponseUtil.getSuccessfulApiResponse(
                    cached, "Complaints listed successfully");
        }


        // fetch from db
        SearchResponseWithMapperBuilder<Complaint, ListComplainsResponse> responseBuilder =
                SearchResponseWithMapperBuilder
                        .<Complaint, ListComplainsResponse>builder()
                        .count(param -> complaintSearchRepository.count(param, municipalityId))
                        .searchData(param -> complaintSearchRepository.getAll(param, municipalityId))
                        .mapperFunction(this.complaintMapper::listComplainsResponses)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListComplainsResponse> response =
                searchResponse.getSearchResponse(responseBuilder);
        // save to redis (TTL is important)
        redisTemplate.opsForValue().set(cacheKey,objectMapper.writeValueAsString(response), Duration.ofMinutes(2));

        LOG.info("Complaints fetched from DB & cache for key {}", cacheKey);
        return ResponseUtil.getSuccessfulApiResponse(response, "Complaints listed successfully");
    }

    @Override
    public ApiResponse<?>  listAssignTo(SearchParam searchParam, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }

        AuthorityUser authorityUser = authorityUserOpt.get();
        Municipality municipality = authorityUser.getMunicipality();

        if (municipality == null) {
            return ResponseUtil.getFailureResponse("Authority user is not assigned to any municipality.");
        }

        Long municipalityId = municipality.getId();

        SearchResponseWithMapperBuilder<AuthorityUser, AssignToListResponse> responseBuilder =
                SearchResponseWithMapperBuilder.<AuthorityUser, AssignToListResponse>builder()
                        .count(authorityUserSearchRepository::count)
                        .searchData(authorityUserSearchRepository::getAll)
                        .mapperFunction(this.complaintMapper::listAllAssignTo)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<AssignToListResponse> response = searchResponse.getSearchResponse(responseBuilder);

        LOG.info("Authority User listed successfully");
        return ResponseUtil.getSuccessfulApiResponse(response, "Authority User listed Successfully");
    }


    @Override
    public ApiResponse<?> assignComplaintToAuthorityUser(ComplaintAssignRequest complaintAssignRequest, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        AuthorityUser authorityUser = authorityUserOpt.get();
        String municipalityUniqueId = authorityUser.getMunicipality().getUniqueId();

        Optional<AuthorityUser> assignToAuthorityUserOpt =
                authorityUserRepository.findByUniqueId(complaintAssignRequest.getAuthorityUserId());

        if (assignToAuthorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by uniqueId {}", complaintAssignRequest.getAuthorityUserId());
            return ResponseUtil.getFailureResponse(" User Not Found.");
        }

        Complaint complaint = complaintRepository
                .findByIdAndMunicipalityId(complaintAssignRequest.getComplaintId(), municipalityUniqueId)
                .orElseThrow(() -> new RuntimeException("Complaint not found or access denied"));
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint rejected. It cannot be assigned.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint Resolved. It cannot be assigned.");
        }
        if((complaint.getAssignedTo() == null)){
            Complaint toBeAssgnedComplaint = complaintMapper.assignedTo(complaint,assignToAuthorityUserOpt.get());
            AuthorityUserEmailLog  userEmailLog = complaintMapper.assignedEmailContent(assignToAuthorityUserOpt.get(),complaint);
            SendEmailRequest sendEmailRequest = new SendEmailRequest();
            sendEmailRequest.setRecipient(assignToAuthorityUserOpt.get().getEmail());
            sendEmailRequest.setSubject(EmailSubjectConstant.ASSIGN_ESCALATION_TO_STAFF);
            sendEmailRequest.setMessage(userEmailLog.getMessage());
            mailService.sendEmail(sendEmailRequest);
            complaintRepository.save(toBeAssgnedComplaint);

        }else {
            return ResponseUtil.getFailureResponse("Complaint already assigned.");
        }



        return ResponseUtil.getSuccessfulApiResponse("Complaint assigned successfully.");
    }

    @Override
    public ApiResponse<?> getComplaint(ComplaintUniqueDto complaintUniqueIdDto, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        AuthorityUser authorityUser = authorityUserOpt.get();
        String municipalityUniqueId = authorityUser.getMunicipality().getUniqueId();

        Complaint complaint = complaintRepository
                .findByIdAndMunicipalityId(complaintUniqueIdDto.getUniqueId(), municipalityUniqueId)
                .orElseThrow(() -> new RuntimeException("Complaint not found or access denied"));

        return ResponseUtil.getSuccessfulApiResponse(complaint, "Complaint fetched");
    }


//    public ApiResponse<?> updateComplaint(UpdateComplaintRequest req, Principal loggedInAdmin) {
//        Optional<AuthorityUser> authorityUserOpt =
//                authorityUserRepository.findByEmail(loggedInAdmin.getName());
//
//        if (authorityUserOpt.isEmpty()) {
//            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
//            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
//        }
//        AuthorityUser authorityUser = authorityUserOpt.get();
//        String municipalityId = authorityUser.getMunicipality().getUniqueId();
//
//        Complaint complaint = complaintRepository
//                .findByIdAndMunicipalityId(req.getUniqueId, municipalityId)
//                .orElseThrow(() -> new RuntimeException("Complaint not found or access denied"));
//
//
//
//        complaintRepository.save(complaint);
//
//        return ResponseUtil.getSuccessfulApiResponse(null, "Complaint updated successfully");
//    }


    @Override
    public ApiResponse<?> closedComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        AuthorityUser authorityUser = authorityUserOpt.get();
        String municipalityUniqueId = authorityUser.getMunicipality().getUniqueId();

        Complaint complaint = complaintRepository
                .findByIdAndMunicipalityId(complaintUniqueIdDto.getUniqueId(), municipalityUniqueId)
                .orElseThrow(() -> new RuntimeException("Complaint not found or access denied"));
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint rejected. It cannot be blocked.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint Resolved. It cannot be blocked.");
        }
        complaint.setActive(false);
        complaint.setUpdatedDate(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.CLOSED.getName()));
        actionLogMapper.closedComplaintMapper(authorityUser.getId(), loggedInAdmin, httpServletRequest,complaintUniqueIdDto.getRemarks());


        complaintRepository.save(complaint);

        return ResponseUtil.getSuccessfulApiResponse("Complaint Closed successfully");
    }

    @Override
    public ApiResponse<?> inProgressComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin) {
        Optional<AuthorityUser> authorityUserOpt =
                authorityUserRepository.findByEmail(loggedInAdmin.getName());

        if (authorityUserOpt.isEmpty()) {
            LOG.error("Failed to find authority user by email {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");
        }
        AuthorityUser authorityUser = authorityUserOpt.get();
        String municipalityUniqueId = authorityUser.getMunicipality().getUniqueId();

        Complaint complaint = complaintRepository
                .findByIdAndMunicipalityId(complaintUniqueIdDto.getUniqueId(), municipalityUniqueId)
                .orElseThrow(() -> new RuntimeException("Complaint not found or access denied"));
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.REJECTED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint rejected. It cannot be update to progress.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.RESOLVED.getName())) {
            return ResponseUtil.getFailureResponse("Complaint Resolved. It cannot be update to progress.");
        }
        if (Objects.equals(complaint.getStatus().getName(), ComplaintStatusConstant.CLOSED.getName())) {
            return ResponseUtil.getFailureResponse("Closed Complaint cannot be update to inProgress.");
        }
        complaint.setResolvedAt(LocalDateTime.now());
        complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.IN_PROGRESS.getName()));

        complaintRepository.save(complaint);

        return ResponseUtil.getSuccessfulApiResponse("Complaint update to PROGRESS status.");
    }



}
