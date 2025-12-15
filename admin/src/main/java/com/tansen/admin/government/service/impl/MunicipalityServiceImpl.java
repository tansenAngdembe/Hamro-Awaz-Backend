package com.tansen.admin.government.service.impl;

import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.admin.government.mapper.AuthorityUserMapper;
import com.tansen.admin.government.mapper.MunicipalityMapper;
import com.tansen.admin.government.service.MunicipalityService;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Municipality;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.MunicipalityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Service
public class MunicipalityServiceImpl implements MunicipalityService {
    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityServiceImpl.class);

    private final MunicipalityRepository municipalityRepository;
    private final AuthorityUserRepository authorityUserRepository;
    private final MunicipalityMapper municipalityMapper;
    private final AuthorityUserMapper authorityUserMapper;
    private final ActionLogMapper actionLogMapper;

    public MunicipalityServiceImpl(MunicipalityRepository municipalityRepository, AuthorityUserRepository authorityUserRepository, MunicipalityMapper municipalityMapper, AuthorityUserMapper authorityUserMapper, ActionLogMapper actionLogMapper) {
        this.municipalityRepository = municipalityRepository;
        this.authorityUserRepository = authorityUserRepository;
        this.municipalityMapper = municipalityMapper;
        this.authorityUserMapper = authorityUserMapper;
        this.actionLogMapper = actionLogMapper;
    }
    @Override
    ApiResponse<?> createMunicipality(CreateMunicipalityRequest createMunicipalityRequest, MultipartFile documentFile, Principal principal, HttpServletRequest httpServletRequest) throws IOException{
        if(municipalityRepository.existsByCode(createMunicipalityRequest.getCode())){
            LOG.error("Municipality already exists with code {}", createMunicipalityRequest.getCode());
            return ResponseUtil.getFailureResponse("Municipality already exists with code " + createMunicipalityRequest.getCode());
        }
        if(municipalityRepository.existsByEmail(createMunicipalityRequest.getEmail())){
            LOG.error("Municipality already exists with email {}", createMunicipalityRequest.getEmail());
            return ResponseUtil.getFailureResponse("Municipality  already exists with email " + createMunicipalityRequest.getEmail());
        }
        if(authorityUserRepository.existsByEmail(createMunicipalityRequest.getAuthorityAdminEmail())){
            LOG.error("Authority user already exists with email {}", createMunicipalityRequest.getAuthorityAdminEmail());
            return ResponseUtil.getFailureResponse("Authority user already exists with email " + createMunicipalityRequest.getAuthorityAdminEmail());
        }
        Municipality  municipality = municipalityMapper.mapToMunicipality(createMunicipalityRequest,documentFile);
        Municipality savedMunicipality  = municipalityRepository.save(municipality);
        LOG.info("Saved Municipality {}", savedMunicipality.getId());
        AuthorityUser authorityUser = authorityUserMapper.mapToAuthorityUser(createMunicipalityRequest,savedMunicipality);
        AuthorityUser savedAuthorityUser = authorityUserRepository.save(authorityUser);

        actionLogMapper.createMunicipality(municipality.getId(),principal,httpServletRequest);
        actionLogMapper.createAuthorityUser(authorityUser.getId(),"Created Admin for Municipality",principal,httpServletRequest);
        LOG.info("Registration email link for Authority user was sent successfully to: {}", savedAuthorityUser.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Municipality Account created successfully.");
    }

}
