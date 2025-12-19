package com.tansen.admin.government.service.impl;

import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.admin.government.dto.request.*;
import com.tansen.admin.government.dto.response.AuthorityUserResponse;
import com.tansen.admin.government.mapper.AuthorityUserMapper;
import com.tansen.admin.government.service.AuthorityUserService;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
import com.tansen.entity.Municipality;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.MunicipalityRepository;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorityUserServiceImpl implements AuthorityUserService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityUserServiceImpl.class);

    private final AuthorityUserRepository authorityUserRepository;
    private final MunicipalityRepository municipalityRepository;
    private final MailService mailService;
    private final ActionLogMapper actionLogMapper;
    private final AuthorityUserMapper authorityUserMapper;
    private final AuthorityUserEmailLogMapper authorityUserEmailLogMapper;
    private final AuthorityUserSearchRepository authorityUserSearchRepository;
    private final SearchResponse searchResponse;

    public AuthorityUserServiceImpl(AuthorityUserRepository authorityUserRepository, MunicipalityRepository municipalityRepository, MailService mailService, ActionLogMapper actionLogMapper, AuthorityUserMapper authorityUserMapper, AuthorityUserEmailLogMapper authorityUserEmailLogMapper, AuthorityUserSearchRepository authorityUserSearchRepository, SearchResponse searchResponse){
        this.authorityUserRepository = authorityUserRepository;
        this.municipalityRepository = municipalityRepository;
        this.mailService = mailService;
        this.actionLogMapper = actionLogMapper;
        this.authorityUserMapper = authorityUserMapper;
        this.authorityUserEmailLogMapper = authorityUserEmailLogMapper;
        this.authorityUserSearchRepository = authorityUserSearchRepository ;
        this.searchResponse = searchResponse;
    }
    @Override
    @Transactional
    public ApiResponse<?> createAuthorityUser(CreateMunicipalityUserRequest createVendorUserRequest, MultipartFile profilePicture, Principal loggedInUser, HttpServletRequest request) throws IOException {
        if (authorityUserRepository.existsByEmail(createVendorUserRequest.getEmail())) {
            LOG.error("Failed to create municipality user. Municipality user with email {} already exists", createVendorUserRequest.getEmail());
            return ResponseUtil.getFailureResponse("Municipality user with this email already exists");
        }
        if (authorityUserRepository.existsByPhoneNumber(createVendorUserRequest.getMobileNumber())) {
            LOG.error("Failed to create municipality user. Municipality user with mobile number {} already exists", createVendorUserRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Municipality user with this mobile number already exists");
        }
        Optional<Municipality> municipality = municipalityRepository.findByUniqueId(createVendorUserRequest.getVendorUniqueId());
        if (municipality.isEmpty()) {
            LOG.info("Failed to create municipality user. Municipality with uniqueId {} not found", createVendorUserRequest.getVendorUniqueId());
            return ResponseUtil.getFailureResponse("Municipality not found");
        }
        AuthorityUser authorityUser = authorityUserMapper.mapCreateAuthorityUserToEntity(createVendorUserRequest, municipality.get(), profilePicture);
        AuthorityUser saveAuthorityUser = authorityUserRepository.save(authorityUser);
        LOG.info("Municipality user with email {} created successfully", createVendorUserRequest.getEmail());

        AuthorityUserEmailLog vendorUserEmailLog = authorityUserEmailLogMapper.mapToVendor(saveAuthorityUser);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(saveAuthorityUser.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.AUTHORITY_USER_ACCOUNT_VERIFICATION_SUBJECT);
        sendEmailRequest.setMessage(vendorUserEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);

        actionLogMapper.createAuthorityUser(Long.valueOf(saveAuthorityUser.getId()), "Authority user created successfully", loggedInUser, request);
        return ResponseUtil.getSuccessfulApiResponse("Authority user created successfully");
    }

    @Override
    public ApiResponse<?> getAuthorityUserList( SearchParam searchParam) {
        SearchResponseWithMapperBuilder<AuthorityUser, AuthorityUserResponse> responseBuilder = SearchResponseWithMapperBuilder.<AuthorityUser, AuthorityUserResponse>builder()
                .count(authorityUserSearchRepository::count).searchData(authorityUserSearchRepository::getAll)
                .mapperFunction(this.authorityUserMapper::getAuthorityUserResponseList).searchParam(searchParam)
                .build();
        PageableResponse<AuthorityUserResponse> response = searchResponse.getSearchResponse(responseBuilder);
        LOG.info("Vendor User list retrieved successfully");
        return ResponseUtil.getSuccessfulApiResponseWithData(response, "Vendor listed successfully");
    }

    @Override
    public ApiResponse<?> viewAuthorityUser(MunicipalityUserRequest request) {
        Optional<AuthorityUser> authorityUser = authorityUserRepository.findByUniqueId(request.getUniqueId());
        if (authorityUser.isEmpty()) {
            LOG.info("Failed to retrieve vendor user. Vendor user with unique ID {} not found", request.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user not found");
        }
        AuthorityUserResponse vendorUserResponse = authorityUserMapper.mapAuthorityUserToResponse(authorityUser.get());
        return ResponseUtil.getSuccessfulApiResponseWithData(vendorUserResponse, "Authority user retrieved successfully");
    }

    @Override
    public ApiResponse<?> editAuthorityUser(EditMunicipalityUserRequest editAuthorityUserRequest, MultipartFile profilePicture, Principal loggedInUser, HttpServletRequest request) throws IOException {
        Optional<AuthorityUser> existingVendorUser = authorityUserRepository.findByUniqueId(editAuthorityUserRequest.getUniqueId());
        if (existingVendorUser.isEmpty()) {
            LOG.error("Failed to edit vendor user. Authority user with unique ID {} not found", editAuthorityUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user not found");
        }
        AuthorityUser user = existingVendorUser.get();
        Optional<AuthorityUser> userWithEmail = authorityUserRepository.findByEmail(editAuthorityUserRequest.getEmail());
        if (userWithEmail.isPresent() && !userWithEmail.get().getId().equals(user.getId())) {
            LOG.info("Failed to edit vendor user. Authority user with email {} already exists", editAuthorityUserRequest.getEmail());
            return ResponseUtil.getFailureResponse("Authority user with this email already exists");
        }
        Optional<AuthorityUser> userWithMobile = authorityUserRepository.findByPhoneNumber(editAuthorityUserRequest.getMobileNumber());
        if (userWithMobile.isPresent() && !userWithMobile.get().getId().equals(user.getId())) {
            LOG.info("Failed to edit vendor user. Authority user with mobile number {} already exists", editAuthorityUserRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Authority user with this mobile number already exists");
        }
        AuthorityUser authorityUser = authorityUserMapper.updateAuthorityUser(editAuthorityUserRequest, user, profilePicture, loggedInUser, request);
        authorityUserRepository.save(authorityUser);
        LOG.info("Vendor user with unique ID {} edited successfully", editAuthorityUserRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Authority user edited successfully");
    }

    @Override
    public ApiResponse<?> blockAuthorityUser(MunicipalityUserActionRequest authorityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        Optional<AuthorityUser> existingVendorUser = authorityUserRepository.findByUniqueId(authorityUserActionRequest.getUniqueId());
        if (existingVendorUser.isEmpty()) {
            LOG.error("Failed to block authority user. Authority user with unique ID {} not found", authorityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user not found");
        }
        if (Objects.equals(StatusConstant.BLOCKED.getName(), existingVendorUser.get().getStatus().getName())) {
            LOG.info("Failed to block authority user. Authority user with unique ID {} is already blocked", authorityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user is already blocked");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), existingVendorUser.get().getStatus().getName())) {
            LOG.info("Failed to block authority user. Authority user with unique ID {} is deleted.", authorityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user is deleted. Cannot block a deleted authority user.");
        }
        AuthorityUser authorityUser = existingVendorUser.get();
        AuthorityUser authorityUsers = authorityUserMapper.blockAuthorityUser(authorityUser, authorityUserActionRequest.getRemarks(), loggedInUser, request);
        authorityUserRepository.save(authorityUsers);
        LOG.info("Authority user with unique ID {} blocked successfully", authorityUserActionRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Authority user blocked successfully");
    }

    @Override
    public ApiResponse<?> unblockAuthorityUser(MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        Optional<AuthorityUser> existingVendorUser = authorityUserRepository.findByUniqueId(municipalityUserActionRequest.getUniqueId());
        if (existingVendorUser.isEmpty()) {
            LOG.error("Failed to unblock authority user. Authority user with unique ID {} not found", municipalityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user not found");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), existingVendorUser.get().getStatus().getName())) {
            LOG.info("Failed to unblock authority user. Authority user with unique ID {} is deleted.", municipalityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user is deleted. Cannot unblock a deleted authority user.");
        }
        if (!Objects.equals(StatusConstant.BLOCKED.getName(), existingVendorUser.get().getStatus().getName())) {
            LOG.info("Failed to unblock authority user. Authority user with unique ID {} is not blocked", municipalityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user is not blocked");
        }
        AuthorityUser authorityUser = existingVendorUser.get();
        AuthorityUser unblockVendorUser = authorityUserMapper.unblockAuthorityUser(authorityUser, municipalityUserActionRequest.getRemarks(), loggedInUser, request);
        authorityUserRepository.save(unblockVendorUser);
        LOG.info("Authority user with unique ID {} unblocked successfully", municipalityUserActionRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Authority user unblocked successfully");
    }

    @Override
    public ApiResponse<?> deleteAuthorityUser(MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        Optional<AuthorityUser> existingVendorUser = authorityUserRepository.findByUniqueId(municipalityUserActionRequest.getUniqueId());
        if (existingVendorUser.isEmpty()) {
            LOG.error("Failed to delete authority user. Authority user with unique ID {} not found", municipalityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user not found");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), existingVendorUser.get().getStatus().getName())) {
            LOG.info("Failed to delete authority user. Authority user with unique ID {} is already deleted", municipalityUserActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Authority user is already deleted");
        }
        AuthorityUser vendorUser = existingVendorUser.get();
        AuthorityUser vendorUsers = authorityUserMapper.deleteAuthorityUserM(vendorUser, municipalityUserActionRequest.getRemarks(), loggedInUser, request);
        authorityUserRepository.save(vendorUsers);
        LOG.info("Authority user with unique ID {} deleted successfully", municipalityUserActionRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Authority user deleted successfully");
    }
}
