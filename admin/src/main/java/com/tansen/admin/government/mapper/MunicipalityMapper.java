package com.tansen.admin.government.mapper;

import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.core.util.AuthorityUserTokenUtil;
import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.admin.government.dto.request.EditMunicipalityRequest;
import com.tansen.admin.government.dto.response.MunicipalityResponse;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.entity.Municipality;
import com.tansen.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MunicipalityMapper {
    private static final Logger LOG = LoggerFactory.getLogger(MunicipalityMapper.class);
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private LocalLevelRepository localLevelRepository;
    @Autowired
    private  UploadFileService uploadFileService;
    @Autowired
    private  ActionLogMapper actionLogMapper;
    @Autowired
    private  StatusRepository statusRepository;
    @Autowired
    private  AuthorityUserRepository authorityUserRepository;
    @Autowired
    private  AuthorityUserTokenRepository authorityUserTokenRepository;

    public Municipality mapToMunicipality(CreateMunicipalityRequest createMunicipalityRequest, MultipartFile documentFile) throws IOException {
        Municipality municipality = new Municipality();
        municipality.setGovernmentName(createMunicipalityRequest.getGovernmentName());
        municipality.setEmail(createMunicipalityRequest.getEmail());
        municipality.setCode(createMunicipalityRequest.getCode());
        municipality.setDescription(createMunicipalityRequest.getDescription());
        municipality.setUniqueId(UuidUtil.generateUuid());
        municipality.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        municipality.setProvince(provinceRepository.findById(Long.valueOf(createMunicipalityRequest.getProvinceId())).orElseThrow(
                () -> new RuntimeException("Province now found with id {}" + createMunicipalityRequest.getProvinceId())));
        municipality.setDistrict(districtRepository.findById(Long.valueOf(createMunicipalityRequest.getDistrictId())).orElseThrow(
                () -> new RuntimeException("District not found with id {}" + createMunicipalityRequest.getDistrictId())));
        municipality.setLocalLevel(localLevelRepository.findById(Long.valueOf(createMunicipalityRequest.getLocalLevelId())).orElseThrow(
                () -> new RuntimeException("Local level repository not  found with id {}" + createMunicipalityRequest.getLocalLevelId())));
        municipality.setLatitude(createMunicipalityRequest.getLatitude());
        municipality.setLongitude(createMunicipalityRequest.getLongitude());
        municipality.setAddress(createMunicipalityRequest.getAddress());
        if (documentFile != null && !documentFile.isEmpty()) {
            municipality.setDocumentUrl(uploadFileService.uploadFile(documentFile,
                    FilePathConstant.BASE_PATH, FilePathConstant.MUNICIPALITY, false));
        }
        return municipality;

    }

    public Municipality updateMunicipality(Municipality municipality, EditMunicipalityRequest request, MultipartFile documentFile, Principal loggedInUser, HttpServletRequest httpServletRequest) throws IOException {
        municipality.setGovernmentName(request.getGovernmentName());
        municipality.setCode(request.getCode());
        municipality.setDescription(request.getDescription());

        municipality.setProvince(provinceRepository.findById(Long.valueOf(request.getProvinceId()))
                .orElseThrow(() -> new RuntimeException("Failed to update government. Province not found with id: " + request.getProvinceId())));

        municipality.setDistrict(districtRepository.findById(Long.valueOf(request.getDistrictId()))
                .orElseThrow(() -> new RuntimeException("Failed to update government. District not found with id: " + request.getDistrictId())));

        municipality.setLocalLevel(localLevelRepository.findById(Long.valueOf(request.getLocalLevelId()))
                .orElseThrow(() -> new RuntimeException("Failed to update government. Local Level not found with id: " + request.getLocalLevelId())));

        municipality.setLatitude(request.getLatitude());
        municipality.setLongitude(request.getLongitude());
        municipality.setAddress(request.getAddress());

        municipality.setDocumentUrl(
                uploadFileService.uploadFile(documentFile, FilePathConstant.BASE_PATH, FilePathConstant.MUNICIPALITY, true)
        );
        actionLogMapper.updateMunicipality(municipality.getId(), loggedInUser, httpServletRequest);

        return municipality;
    }

    public abstract MunicipalityResponse mapToMunicipalityResponse(Municipality municipality);

    public List<MunicipalityResponse> getMunicipalityResponseList(List<Municipality> municipalities) {

        return municipalities.stream().map(this::mapToMunicipalityResponse).toList();
    }

    public Municipality blockMunicipality(Municipality municipality, String remark, Principal loggedInUser, HttpServletRequest request) {
        loggingOutMunicipalityUser(municipality);
        municipality.setStatus(statusRepository.findByName(StatusConstant.BLOCKED.getName()));
        municipality.setUpdatedAt(LocalDateTime.now());
        actionLogMapper.blockMunicipality(Long.valueOf(municipality.getId()), remark, loggedInUser, request);
        return municipality;
    }

    public Municipality unblockMunicipality(Municipality municipality, String remark, Principal loggedInUser, HttpServletRequest request) {
        municipality.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        municipality.setUpdatedAt(LocalDateTime.now());
        actionLogMapper.unblockMunicipality(Long.valueOf(municipality.getId()), remark, loggedInUser, request);
        return municipality;
    }

    public Municipality deleteMunicipality(Municipality municipality, String remark, Principal loggedInUser, HttpServletRequest request) {
        loggingOutMunicipalityUser(municipality);
        municipality.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
        municipality.setUpdatedAt(LocalDateTime.now());
        actionLogMapper.deleteMunicipality(Long.valueOf(municipality.getId()), remark, loggedInUser, request);
        return municipality;
    }

    private void loggingOutMunicipalityUser(Municipality municipality) {
        List<AuthorityUser> authorityUsers = authorityUserRepository.findByMunicipality(municipality);
        if (!authorityUsers.isEmpty()) {
            for (AuthorityUser authorityUser : authorityUsers) {
                List<AuthorityUserToken> activeToken = authorityUserTokenRepository.findByAuthorityUserAndLoggedOutFalse(authorityUser);
                if (!activeToken.isEmpty()) {
                    LOG.info("Invalidating {} active token(s) for admin with id: {}", activeToken.size(), authorityUser.getId());
                    for (AuthorityUserToken token : activeToken) {
                        AuthorityUserTokenUtil.invalidateToken(token.getRefreshToken(),
                                authorityUserTokenRepository::findByRefreshToken,
                                authorityUserTokenRepository
                        );
                    }
                }
            }
        }
    }
}
