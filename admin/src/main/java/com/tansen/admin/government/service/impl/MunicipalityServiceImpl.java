package com.tansen.admin.government.service.impl;

import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.admin.government.controller.MunicipalityController;
import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.admin.government.dto.request.EditMunicipalityRequest;
import com.tansen.admin.government.dto.request.MunicipalityActionRequest;
import com.tansen.admin.government.dto.request.MunicipalityRequest;
import com.tansen.admin.government.dto.response.MunicipalityResponse;
import com.tansen.admin.government.mapper.AuthorityUserMapper;
import com.tansen.admin.government.mapper.MunicipalityMapper;
import com.tansen.admin.government.service.MunicipalityService;
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
import com.tansen.repository.searchrepo.MunicipalitySearchRepository;
import com.tansen.repository.searchrepo.impl.MunicipalitySearchRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
public class MunicipalityServiceImpl implements MunicipalityService {
    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityServiceImpl.class);

    private final MunicipalityRepository municipalityRepository;
    private final AuthorityUserRepository authorityUserRepository;
    private final MunicipalityMapper municipalityMapper;
    private final AuthorityUserMapper authorityUserMapper;
    private final ActionLogMapper actionLogMapper;
    private final MunicipalitySearchRepository municipalitySearchRepository;
    private final SearchResponse searchResponse;
    private final AuthorityUserEmailLogMapper authorityUserEmailLogMapper;
    private final MailService mailService;

    public MunicipalityServiceImpl(MunicipalityRepository municipalityRepository, AuthorityUserRepository authorityUserRepository, MunicipalityMapper municipalityMapper, AuthorityUserMapper authorityUserMapper, ActionLogMapper actionLogMapper, MunicipalitySearchRepositoryImpl municipalitySearchRepository, SearchResponse searchResponse, AuthorityUserEmailLogMapper authorityUserEmailLogMapper, MailService mailService) {
        this.municipalityRepository = municipalityRepository;
        this.authorityUserRepository = authorityUserRepository;
        this.municipalityMapper = municipalityMapper;
        this.authorityUserMapper = authorityUserMapper;
        this.actionLogMapper = actionLogMapper;
        this.municipalitySearchRepository = municipalitySearchRepository;
        this.searchResponse = searchResponse;
        this.authorityUserEmailLogMapper = authorityUserEmailLogMapper;
        this.mailService = mailService;
    }

    @Override
    public ApiResponse<?> createMunicipality(CreateMunicipalityRequest createMunicipalityRequest, MultipartFile documentFile, Principal principal, HttpServletRequest httpServletRequest) throws IOException {
        if (municipalityRepository.existsByCode(createMunicipalityRequest.getCode())) {
            LOG.error("Municipality already exists with code {}", createMunicipalityRequest.getCode());
            return ResponseUtil.getFailureResponse("Municipality already exists with code " + createMunicipalityRequest.getCode());
        }
        if (municipalityRepository.existsByEmail(createMunicipalityRequest.getEmail())) {
            LOG.error("Municipality already exists with email {}", createMunicipalityRequest.getEmail());
            return ResponseUtil.getFailureResponse("Municipality  already exists with email " + createMunicipalityRequest.getEmail());
        }
        if (authorityUserRepository.existsByEmail(createMunicipalityRequest.getAuthorityAdminEmail())) {
            LOG.error("Authority user already exists with email {}", createMunicipalityRequest.getAuthorityAdminEmail());
            return ResponseUtil.getFailureResponse("Authority user already exists with email " + createMunicipalityRequest.getAuthorityAdminEmail());
        }
        Municipality municipality = municipalityMapper.mapToMunicipality(createMunicipalityRequest, documentFile);
        Municipality savedMunicipality = municipalityRepository.save(municipality);
        LOG.info("Saved Municipality {}", savedMunicipality.getId());
        AuthorityUser authorityUser = authorityUserMapper.mapToAuthorityUser(createMunicipalityRequest, savedMunicipality);
        AuthorityUser savedAuthorityUser = authorityUserRepository.save(authorityUser);


        AuthorityUserEmailLog authorityUserEmailLog = authorityUserEmailLogMapper.mapToAuthorityUser(savedAuthorityUser);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(savedAuthorityUser.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.AUTHORITY_USER_ACCOUNT_VERIFICATION_SUBJECT);
        sendEmailRequest.setMessage(authorityUserEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);


        actionLogMapper.createMunicipality(municipality.getId(), principal, httpServletRequest);
        actionLogMapper.createAuthorityUser(authorityUser.getId(), "Created Admin for Municipality", principal, httpServletRequest);
        LOG.info("Registration email link for Authority user was sent successfully to: {}", savedAuthorityUser.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Municipality Account created successfully.");
    }

    @Override
    public ApiResponse<?> editMunicipality(EditMunicipalityRequest editMunicipalityRequest, MultipartFile documentFile, Principal loggedInUser, HttpServletRequest httpServletRequest) throws IOException {
        Optional<Municipality> existingMunicipality = municipalityRepository.findByUniqueId(editMunicipalityRequest.getUniqueId());
        if (existingMunicipality.isEmpty()) {
            LOG.error("Failed to update Municipality. Municipality with unique ID {} not found", editMunicipalityRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality not found");
        }
        if (municipalityRepository.existsByCode(editMunicipalityRequest.getCode())) {
            LOG.error("Failed to update government. Government with code number {} already exists", editMunicipalityRequest.getCode());
            return ResponseUtil.getFailureResponse("Government with this code number already exists");
        }

        Municipality municipality = existingMunicipality.get();
        Municipality updatedMunicipality = municipalityMapper.updateMunicipality(municipality, editMunicipalityRequest, documentFile, loggedInUser, httpServletRequest);
        municipalityRepository.save(updatedMunicipality);
        LOG.info("Government with unique ID {} updated successfully", editMunicipalityRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Government updated successfully.");
    }

    @Override
    public ApiResponse<?> getMunicipalityList(SearchParam searchParam) {
        SearchResponseWithMapperBuilder<Municipality, MunicipalityResponse> responseBuilder = SearchResponseWithMapperBuilder.<Municipality, MunicipalityResponse>builder()
                .count(municipalitySearchRepository::count).searchData(municipalitySearchRepository::getAll)
                .mapperFunction(this.municipalityMapper::getMunicipalityResponseList).searchParam(searchParam)
                .build();
        PageableResponse<MunicipalityResponse> response = searchResponse.getSearchResponse(responseBuilder);
        LOG.info("Municipality list retrieved successfully");
        return ResponseUtil.getSuccessfulApiResponseWithData(response, "Municipality listed successfully");
    }
    @Override
    public ApiResponse<?> viewMunicipalityDetails(MunicipalityRequest request) {
        Optional<Municipality> existingVendor = municipalityRepository.findByUniqueId(request.getUniqueId());
        if (existingVendor.isEmpty()){
            LOG.error("Failed to view municipality details. Municipality with unique ID {} not found", request.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality not found");
        }
        Municipality municipality = existingVendor.get();
        MunicipalityResponse vendorResponse = municipalityMapper.mapToMunicipalityResponse(municipality);
        return ResponseUtil.getSuccessfulApiResponseWithData(vendorResponse, "Municipality details retrieved successfully.");
    }

    @Override
    public ApiResponse<?> blockMunicipality(MunicipalityActionRequest blockMunicipalityRequest, HttpServletRequest request, Principal loggedInUser) {
        Optional<Municipality> existingMunicipalityByUniqueId = municipalityRepository.findByUniqueId(blockMunicipalityRequest.getUniqueId());
        if (existingMunicipalityByUniqueId.isEmpty()) {
            LOG.error("Failed to block municipality. Municipality with unique ID {} not found", blockMunicipalityRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor not found");
        }
        if (Objects.equals(StatusConstant.BLOCKED.getName(), existingMunicipalityByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block municipality. Municipality with unique ID {} is already blocked", blockMunicipalityRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is already blocked");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), existingMunicipalityByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block municipality. Municipality with unique ID {} is deleted.", blockMunicipalityRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is deleted. Cannot block a deleted municipality.");
        }
        if (Objects.equals(StatusConstant.PENDING.getName(), existingMunicipalityByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block municipality. Municipality with unique ID {} is still pending.", blockMunicipalityRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is still in pending state. Cannot block a pending  municipality.");
        }
        Municipality municipality = existingMunicipalityByUniqueId.get();
        Municipality updatedMunicipality = municipalityMapper.blockMunicipality(municipality, blockMunicipalityRequest.getRemarks(), loggedInUser, request);
        municipalityRepository.save(updatedMunicipality);
        LOG.info("Municipality with unique ID {} blocked successfully", blockMunicipalityRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Municipality blocked successfully.");
    }

    @Override
    public ApiResponse<?> unblockMunicipality(MunicipalityActionRequest municipalityActionRequest, HttpServletRequest request, Principal loggedInUser) {
        Optional<Municipality> existingMunicipalityByUniqueId = municipalityRepository.findByUniqueId(municipalityActionRequest.getUniqueId());
        if (existingMunicipalityByUniqueId.isEmpty()) {
            LOG.error("Failed to unblock municipality. Municipality with unique ID {} not found", municipalityActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality not found");
        }
        Municipality municipality = existingMunicipalityByUniqueId.get();
        if (!Objects.equals(StatusConstant.BLOCKED.getName(), municipality.getStatus().getName())) {
            LOG.error("Failed to unblock municipality. Municipality with unique ID {} is not blocked", municipalityActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is not blocked");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), municipality.getStatus().getName())) {
            LOG.error("Failed to unblock municipality. Municipality with unique ID {} is deleted.", municipalityActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is deleted. Cannot unblock a deleted municipality.");
        }
        Municipality updatedMunicipality = municipalityMapper.unblockMunicipality(municipality, municipalityActionRequest.getRemarks(), loggedInUser, request);
        municipalityRepository.save(updatedMunicipality);
        LOG.info("Municipality with unique ID {} unblocked successfully", municipalityActionRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Municipality unblocked successfully.");
    }

    @Override
    public ApiResponse<?> deleteMunicipality(MunicipalityActionRequest municipalityActionRequest, HttpServletRequest request, Principal loggedInUser) {
        Optional<Municipality> existingMunicipalityByUniqueId = municipalityRepository.findByUniqueId(municipalityActionRequest.getUniqueId());
        if (existingMunicipalityByUniqueId.isEmpty()) {
            LOG.error("Failed to delete municipality. Municipality with unique ID {} not found", municipalityActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality not found");
        }
        Municipality municipality = existingMunicipalityByUniqueId.get();
        if (Objects.equals(StatusConstant.DELETED.getName(), municipality.getStatus().getName())) {
            LOG.error("Failed to delete municipality. Municipality with unique ID {} is already deleted", municipalityActionRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Municipality is already deleted");
        }

        Municipality updatedMunicipality = municipalityMapper.deleteMunicipality(municipality, municipalityActionRequest.getRemarks(), loggedInUser, request);
        municipalityRepository.save(updatedMunicipality);
        LOG.info("Municipality with unique ID {} deleted successfully", municipalityActionRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Municipality deleted successfully.");
    }

}
