package com.tansen.admin.government.mapper;

import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.core.util.AuthorityUserTokenUtil;
import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.admin.government.dto.request.CreateMunicipalityUserRequest;
import com.tansen.admin.government.dto.request.EditMunicipalityUserRequest;
import com.tansen.admin.government.dto.response.AuthorityUserResponse;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.entity.Municipality;
import com.tansen.repository.AuthorityAccessGroupRepository;
import com.tansen.repository.AuthorityUserTokenRepository;
import com.tansen.repository.StatusRepository;
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
public abstract class AuthorityUserMapper {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityUserMapper.class);
    @Autowired
    private  AuthorityAccessGroupRepository authorityAccessGroupRepository;
    @Autowired
    private  StatusRepository statusRepository;
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ActionLogMapper actionLogMapper;
    @Autowired
    private AuthorityUserTokenRepository authorityUserTokenRepository;


    public AuthorityUser mapToAuthorityUser(CreateMunicipalityRequest authorityUserRequest, Municipality municipality) {
        AuthorityUser authorityUser = new AuthorityUser();
        authorityUser.setName(authorityUserRequest.getAuthorityAdminFullName());
        authorityUser.setEmail(authorityUserRequest.getAuthorityAdminEmail());
        authorityUser.setPhoneNumber(authorityUserRequest.getAuthorityAdminPhoneNumber());
        authorityUser.setAddress(authorityUserRequest.getAuthorityAdminAddress());
        authorityUser.setAuthorityAccessGroup(authorityAccessGroupRepository.findById(1L).orElseThrow(
                ()-> new RuntimeException("Default Access Group Not Found")
        ));
        authorityUser.setMunicipality(municipality);
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        authorityUser.setCreatedAt(LocalDateTime.now());
        authorityUser.setAuthorityAdmin(true);
        return authorityUser;
    }

    public AuthorityUser mapCreateAuthorityUserToEntity(CreateMunicipalityUserRequest request, Municipality municipality, MultipartFile profilePicture) throws IOException {
        AuthorityUser authorityUser = new AuthorityUser();
        authorityUser.setName(request.getFullName());
        authorityUser.setEmail(request.getEmail());
        authorityUser.setPhoneNumber(request.getMobileNumber());
        authorityUser.setAddress(request.getAddress());
        authorityUser.setAuthorityAccessGroup(authorityAccessGroupRepository.findByName((request.getAuthorityAccessGroupName())).orElseThrow(
                () -> new RuntimeException("Default Authority Access Group not found")
        ));
        authorityUser.setUniqueId(UuidUtil.generateUuid());
        authorityUser.setMunicipality(municipality);
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        authorityUser.setIsActive(false);
        authorityUser.setAuthorityAdmin(false);

        if (profilePicture != null && !profilePicture.isEmpty()) {
            authorityUser.setProfilePictureName(
                    uploadFileService.uploadFile( profilePicture, FilePathConstant.BASE_PATH, FilePathConstant.AUTHORITY_USER, true)
            );
        }

        return authorityUser;
    }

    public AuthorityUser updateAuthorityUser(EditMunicipalityUserRequest request, AuthorityUser authorityUser, MultipartFile profilePicture, Principal loggedInUser, HttpServletRequest httpServletRequest) throws IOException {
        authorityUser.setName(request.getFullName());
        authorityUser.setEmail(request.getEmail());
        authorityUser.setPhoneNumber(request.getMobileNumber());
        authorityUser.setAddress(request.getAddress());
        authorityUser.setAuthorityAccessGroup(authorityAccessGroupRepository.findByName(request.getAuthorityAccessGroupName()).orElseThrow(
                () -> new RuntimeException("Authority Access Group not found with ID: " + request.getAuthorityAccessGroupName())
        ));
        if (profilePicture != null && !profilePicture.isEmpty()) {
            authorityUser.setProfilePictureName(
                    uploadFileService.uploadFile(profilePicture, FilePathConstant.BASE_PATH, FilePathConstant.AUTHORITY_USER, true)
            );
        }
        actionLogMapper.editMunicipalityUser(authorityUser.getId(), "Authority user update.", loggedInUser, httpServletRequest);
        loggingOutAuthorityUser(authorityUser);
        return authorityUser;
    }


    public AuthorityUser blockAuthorityUser(AuthorityUser authorityUser, String remark, Principal loggedInUser, HttpServletRequest request) {
        loggingOutAuthorityUser(authorityUser);
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.BLOCKED.getName()));
        actionLogMapper.blockAuthorityUser(Long.valueOf(authorityUser.getId()), remark, loggedInUser, request);
        return authorityUser;
    }

    public AuthorityUser unblockAuthorityUser(AuthorityUser authorityUser, String remark, Principal loggedInUser, HttpServletRequest request) {
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        actionLogMapper.unblockAuthorityUser(Long.valueOf(authorityUser.getId()), remark, loggedInUser, request);
        return authorityUser;
    }

    public AuthorityUser deleteAuthorityUserM(AuthorityUser authorityUser, String remark, Principal loggedInUser, HttpServletRequest request) {
        loggingOutAuthorityUser(authorityUser);
        authorityUser.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
//        actionLogMapper.deleteVendor(Long.valueOf(authorityUser.getId()), remark, loggedInUser, request);
        return authorityUser;
    }

    public abstract AuthorityUserResponse mapAuthorityUserToResponse(AuthorityUser authorityUser);

    public List<AuthorityUserResponse> getAuthorityUserResponseList(List<AuthorityUser> authorityUsers) {
        return authorityUsers.stream()
                .map(this::mapAuthorityUserToResponse)
                .toList();
    }


    private void loggingOutAuthorityUser(AuthorityUser authorityUser) {
        List<AuthorityUserToken> activeToken = authorityUserTokenRepository.findByAuthorityUserAndLoggedOutFalse(authorityUser);
        if (!activeToken.isEmpty()) {
            LOG.info("Invalidating {} active token(s) for authority user with id: {}", activeToken.size(), authorityUser.getId());
            for (AuthorityUserToken token : activeToken) {
                AuthorityUserTokenUtil.invalidateToken(token.getRefreshToken(),
                        authorityUserTokenRepository::findByRefreshToken,
                        authorityUserTokenRepository
                );
            }

        }
    }

}
