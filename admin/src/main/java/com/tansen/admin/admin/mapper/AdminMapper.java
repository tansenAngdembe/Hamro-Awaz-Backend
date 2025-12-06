package com.tansen.admin.admin.mapper;


import com.tansen.admin.actionlog.dto.ActionLogModel;
import com.tansen.admin.actionlog.service.ActionLogService;
import com.tansen.admin.admin.dto.request.*;
import com.tansen.admin.admin.dto.response.ListAdminResponse;
import com.tansen.admin.admin.dto.response.ViewAdminDetailResponse;
import com.tansen.admin.admin.dto.response.ViewProfileResponse;
import com.tansen.admin.emaillog.mapper.AdminEmailLogMapper;
import com.tansen.admin.util.AdminTokenUtil;
import com.tansen.common.constant.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.IpUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.Admin;
import com.tansen.entity.AdminEmailLog;
import com.tansen.entity.AdminToken;
import com.tansen.repository.AccessGroupRepository;
import com.tansen.repository.AdminRepository;
import com.tansen.repository.AdminTokenRepository;
import com.tansen.repository.StatusRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AdminMapper {
    @Autowired
    protected StatusRepository statusRepository;
    @Autowired
    protected MailService mailService;
    @Autowired
    protected AccessGroupRepository accessGroupRepository;
    @Autowired
    protected AdminRepository adminRepository;
    @Autowired
    private AdminEmailLogMapper adminEmailLogMapper;
    @Autowired
    private ActionLogService actionLogService;
    @Autowired
    private AdminTokenRepository adminTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOG = LoggerFactory.getLogger(AdminMapper.class);
    @Autowired
    private UploadFileService uploadFileService;

    public Admin mapToEntity(CreateAdminRequest createAdminRequest) {
        Admin admin = new Admin();
        admin.setName(createAdminRequest.getName());
        admin.setEmail(createAdminRequest.getEmail());
        admin.setMobileNumber(createAdminRequest.getMobileNumber());
        admin.setAddress(createAdminRequest.getAddress());
        admin.setAccessGroup(accessGroupRepository.findByName(createAdminRequest.getAccessGroup().getName()).orElseThrow());
        admin.setUsername(createAdminRequest.getEmail());
        admin.setUniqueId(UuidUtil.generateUuid());
        admin.setActive(false);
        admin.setSuperAdmin(false);
        admin.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        admin.setCreatedAt(LocalDateTime.now());
        Admin savedAdmin = adminRepository.save(admin);

        AdminEmailLog adminEmailLog = adminEmailLogMapper.mapToAdmin(admin);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(savedAdmin.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.ADMIN_ACCOUNT_VERIFICATION_SUBJECT);
        sendEmailRequest.setMessage(adminEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
        return admin;
    }

    public abstract ListAdminResponse entityToResponse(Admin admin);

    public List<ListAdminResponse> listAllAdmins(List<Admin> admins) {
        return admins.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public void blockAdmin(Admin existingAdmin, BlockAdminRequest blockAdminRequest, HttpServletRequest request, Principal admin) {
        existingAdmin.setStatus(statusRepository.findByName(StatusConstant.BLOCKED.getName()));
        existingAdmin.setActive(false);
        List<AdminToken> activeTokens = adminTokenRepository.findByAdminAndLoggedOutFalse(existingAdmin);
        if (!activeTokens.isEmpty()) {
            for (AdminToken token : activeTokens) {
                AdminTokenUtil.invalidateToken(
                        token.getRefreshToken(),
                        adminTokenRepository::findByRefreshToken,
                        adminTokenRepository
                );
            }
        } else {
            LOG.info("No active tokens found for admin with uniqueId: {}", blockAdminRequest.getUniqueId());
        }

        Admin blockedAdmin = adminRepository.save(existingAdmin);

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(blockAdminRequest.getRemarks());
        actionLogModel.setActionType(ActionTypeConstant.BLOCK);
        actionLogModel.setTargetType(TargetTypeConstant.ADMIN);
        actionLogModel.setTargetId(blockedAdmin.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLogModel, admin);
    }

    public void updateAdmin(Admin existingAdmin, UpdateAdminDetailRequest updateAdminDetailRequest, HttpServletRequest request, Principal admin) {
        if (!Objects.equals(updateAdminDetailRequest.getEmail(), existingAdmin.getEmail())) {
            List<AdminToken> activeTokens = adminTokenRepository.findByAdminAndLoggedOutFalse(existingAdmin);
            if (!activeTokens.isEmpty()) {
                LOG.info("Invalidating {} active token(s) for admin with uniqueId: {}", activeTokens.size(), updateAdminDetailRequest.getUniqueId());
                for (AdminToken token : activeTokens) {
                    AdminTokenUtil.invalidateToken(
                            token.getRefreshToken(),
                            adminTokenRepository::findByRefreshToken,
                            adminTokenRepository
                    );
                }
            } else {
                LOG.info("No active tokens found for admin with uniqueId: {}", updateAdminDetailRequest.getUniqueId());
            }
        }
        existingAdmin.setEmail(updateAdminDetailRequest.getEmail());
        existingAdmin.setName(updateAdminDetailRequest.getName());
        existingAdmin.setMobileNumber(updateAdminDetailRequest.getMobileNumber());
        existingAdmin.setAddress(updateAdminDetailRequest.getAddress());
        existingAdmin.setAccessGroup(accessGroupRepository.findByName(updateAdminDetailRequest.getAccessGroup().getName()).orElseThrow());
//        existingAdmin.setUpdatedAt(LocalDateTime.now());
        Admin updateAdmin = adminRepository.save(existingAdmin);

        ActionLogModel actionLog = new ActionLogModel();
        actionLog.setRemarks(updateAdminDetailRequest.getRemarks());
        actionLog.setTargetType(TargetTypeConstant.ADMIN);
        actionLog.setActionType(ActionTypeConstant.UPDATE);
        actionLog.setTargetId(updateAdmin.getId());
        actionLog.setIpAddress(IpUtil.getClientIp(request));
        actionLogService.insertActionLog(actionLog, admin);
    }

    public Admin deleteAdmin(Admin admin) {
        admin.setStatus(statusRepository.findByName(StatusConstant.DELETED.getName()));
//        admin.setUpdatedAt(new Date());
        return admin;
    }

    public abstract ViewAdminDetailResponse entityToViewDetails(Admin admin);

    public void unblockAdmin(Admin admin, UnblockAdminRequest unblockAdminRequest, HttpServletRequest httpServletRequest, Principal loggedIn)  {
        admin.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        admin.setActive(true);
        Admin unblockAdmin = adminRepository.save(admin);

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(unblockAdminRequest.getRemarks());
        actionLogModel.setActionType(ActionTypeConstant.UNBLOCK);
        actionLogModel.setTargetType(TargetTypeConstant.ADMIN);
        actionLogModel.setTargetId(unblockAdmin.getId());
        actionLogModel.setIpAddress(IpUtil.getClientIp(httpServletRequest));
        actionLogService.insertActionLog(actionLogModel, loggedIn);
    }

    public Admin setPassword(Admin admin, SetPasswordRequest setPasswordRequest) {
        admin.setPassword(passwordEncoder.encode(setPasswordRequest.getPassword()));
//        admin.setPasswordChangeDate(LocalDateTime.now());
        admin.setActive(true);
        admin.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        return admin;
    }

    public Admin editProfile(MultipartFile profilePicture, Admin adminToEditProfile, EditProfileRequest editProfileRequest, Principal principal, HttpServletRequest httpServletRequest)  throws IOException {
        adminToEditProfile.setName(editProfileRequest.getName());
        adminToEditProfile.setMobileNumber(editProfileRequest.getMobileNumber());
        adminToEditProfile.setAddress(editProfileRequest.getAddress());
//        adminToEditProfile.setUpdatedAt(LocalDateTime.now());
        if (profilePicture != null) {
            adminToEditProfile.setProfilePictureName(uploadFileService.uploadFile(profilePicture, FilePathConstant.BASE_PATH, FilePathConstant.ADMIN, true));
        }

        ActionLogModel editProfileLog = new ActionLogModel();
        editProfileLog.setRemarks("EDIT PROFILE");
        editProfileLog.setTargetType(TargetTypeConstant.ADMIN);
        editProfileLog.setActionType(ActionTypeConstant.UPDATE);
        editProfileLog.setIpAddress(IpUtil.getClientIp(httpServletRequest));
        editProfileLog.setTargetId(adminToEditProfile.getId());
        actionLogService.insertActionLog(editProfileLog, principal);
        return adminToEditProfile;
    }

    public abstract ViewProfileResponse viewAdmin(Admin admin);
}
