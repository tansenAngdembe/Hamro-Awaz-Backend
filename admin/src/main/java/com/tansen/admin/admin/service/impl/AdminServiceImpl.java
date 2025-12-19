package com.tansen.admin.admin.service.impl;

import com.tansen.admin.actionlog.dto.ActionLogModel;
import com.tansen.admin.actionlog.mapper.ActionLogMapper;
import com.tansen.admin.actionlog.service.ActionLogService;
import com.tansen.admin.admin.dto.request.*;
import com.tansen.admin.admin.dto.response.ListAdminResponse;
import com.tansen.admin.admin.dto.response.ViewAdminDetailResponse;
import com.tansen.admin.admin.dto.response.ViewProfileResponse;
import com.tansen.admin.admin.mapper.AdminMapper;
import com.tansen.admin.admin.service.AdminService;
import com.tansen.admin.emaillog.mapper.AdminEmailLogMapper;
import com.tansen.admin.util.AdminTokenUtil;
import com.tansen.common.constant.ActionTypeConstant;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.constant.TargetTypeConstant;
import com.tansen.common.dto.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.Admin;
import com.tansen.entity.AdminEmailLog;
import com.tansen.entity.AdminToken;
import com.tansen.repository.AdminEmailLogRepository;
import com.tansen.repository.AdminRepository;
import com.tansen.repository.AdminTokenRepository;
import com.tansen.repository.searchrepo.AdminSearchRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger LOG = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final ActionLogService actionLogService;
    private final AdminSearchRepository adminSearchRepository;
    private final SearchResponse searchResponse;
    private final AdminTokenRepository adminTokenRepository;
    private final AdminEmailLogRepository adminEmailLogRepository;
    private final AdminEmailLogMapper adminEmailLogMapper;
    private final MailService mailService;
    private final ActionLogMapper actionLogMapper;

    public AdminServiceImpl(PasswordEncoder passwordEncoder, AdminRepository adminRepository, AdminMapper adminMapper, ActionLogService actionLogService, AdminSearchRepository adminSearchRepository,
                            SearchResponse searchResponse, AdminTokenRepository adminTokenRepository, AdminEmailLogRepository adminEmailLogRepository, AdminEmailLogMapper adminEmailLogMapper, MailService mailService, ActionLogMapper actionLogMapper) {
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
        this.actionLogService = actionLogService;
        this.adminSearchRepository = adminSearchRepository;
        this.searchResponse = searchResponse;
        this.adminTokenRepository = adminTokenRepository;
        this.adminEmailLogRepository = adminEmailLogRepository;
        this.adminEmailLogMapper = adminEmailLogMapper;
        this.mailService = mailService;
        this.actionLogMapper = actionLogMapper;
    }

    @Override
    @Transactional
    public ApiResponse<?> createAdmin(CreateAdminRequest createAdminRequest, Principal admin, HttpServletRequest httpServletRequest) {
        Admin existingAdminByEmail = adminRepository.findByEmail(createAdminRequest.getEmail());
        if (existingAdminByEmail != null) {
            LOG.error("Admin with email {} already exists", createAdminRequest.getEmail());
            return ResponseUtil.getFailureResponse("Admin with this email already exists");
        }
        Optional<Admin> existingAdminByPhone = adminRepository.findByMobileNumber(createAdminRequest.getMobileNumber());
        if (existingAdminByPhone.isPresent()) {
            LOG.error("Admin with mobileNumber {} already exists", createAdminRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Admin with this mobile number already exists");
        }

        Admin createdAdmin = adminMapper.mapToEntity(createAdminRequest);

        com.tansen.admin.actionlog.dto.ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks("ADMIN CREATED");
        actionLogModel.setActionType(ActionTypeConstant.CREATE);
        actionLogModel.setTargetType(TargetTypeConstant.ADMIN);
        actionLogModel.setTargetId(createdAdmin.getId());
        actionLogModel.setIpAddress(httpServletRequest.getRemoteAddr());
        actionLogService.insertActionLog(actionLogModel, admin);

        LOG.info("Admin created with email :{}", createAdminRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Admin created successfully");
    }

    @Override
    public ApiResponse<?> listAllAdmins(SearchParam searchParam) {
        SearchResponseWithMapperBuilder<Admin, ListAdminResponse> responseBuilder = SearchResponseWithMapperBuilder.<Admin, ListAdminResponse>builder()
                .count(adminSearchRepository::count).searchData(adminSearchRepository::getAll)
                .mapperFunction(this.adminMapper::listAllAdmins).searchParam(searchParam).build();
        PageableResponse<ListAdminResponse> response = searchResponse.getSearchResponse(responseBuilder);
        LOG.info("Admins listed successfully");
        return ResponseUtil.getSuccessfulApiResponseWithData(response, "Admin listed successfully");
    }

    @Override
    public ApiResponse<?> blockAdmin(BlockAdminRequest blockAdminRequest, HttpServletRequest request, Principal admin) {
        Optional<Admin> existingAdminByUniqueId = adminRepository.findByUniqueId(blockAdminRequest.getUniqueId());
        if (existingAdminByUniqueId.isEmpty()) {
            LOG.error("Failed to block admin. Admin with uniqueId {} does not exist", blockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        String loggedInAdminUniqueId = admin.getName();

        if (Objects.equals(loggedInAdminUniqueId, blockAdminRequest.getUniqueId())) {
            LOG.error("Failed to block admin. Admin cannot block themselves.");
            return ResponseUtil.getFailureResponse("Cannot block yourself.");
        }
        if (Objects.equals(StatusConstant.BLOCKED.getName(), existingAdminByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block admin. Admin is already blocked - {}", blockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin is already blocked");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), existingAdminByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block admin. Deleted admin cannot be blocked {}", blockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Deleted admin cannot be blocked");
        }
        if (Objects.equals(StatusConstant.PENDING.getName(), existingAdminByUniqueId.get().getStatus().getName())) {
            LOG.error("Failed to block admin. Pending admin cannot be blocked {}", blockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Pending admin cannot be blocked");
        }
        if (!existingAdminByUniqueId.get().isSuperAdmin()) {
            adminMapper.blockAdmin(existingAdminByUniqueId.get(), blockAdminRequest, request, admin);
            LOG.info("Admin blocked successfully - {}", blockAdminRequest.getUniqueId());
            return ResponseUtil.getSuccessfulApiResponse("Admin blocked successfully");
        }
        LOG.error("Failed to block admin. Super admin cannot be blocked - {}", blockAdminRequest.getUniqueId());
        return ResponseUtil.getFailureResponse("Super Admin cannot be blocked");
    }

    @Override
    public ApiResponse<?> updateAdmin(UpdateAdminDetailRequest updateAdminDetailRequest, HttpServletRequest
            request, Principal loggedInAdmin) {
        Optional<Admin> admin = adminRepository.findByUniqueId(updateAdminDetailRequest.getUniqueId());
        if (admin.isEmpty()) {
            LOG.info("Failed to update admin. Admin with uniqueId {} does not exist", updateAdminDetailRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin not found.");
        }
        Admin existingAdmin = admin.get();
        if (existingAdmin.getStatus().getName().equals(StatusConstant.DELETED.getName())) {
            LOG.info("Failed to update admin. Admin with uniqueId {} is deleted", updateAdminDetailRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Deleted admin cannot be updated");
        }
        if (Objects.equals(existingAdmin.getUniqueId(), updateAdminDetailRequest.getUniqueId())) {
            LOG.error("Failed to update admin .Admin cannot update themself.");
            return ResponseUtil.getFailureResponse("Cannot update yourself.");
        }
        if (existingAdmin.getStatus().getName().equals(StatusConstant.BLOCKED.getName())) {
            LOG.info("Failed to update admin. Admin with uniqueId {} is blocked", updateAdminDetailRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Blocked admin cannot be updated");
        }
        Optional<Admin> adminFromDbWithSameMobile = adminRepository.findByMobileNumber(updateAdminDetailRequest.getMobileNumber());
        if (adminFromDbWithSameMobile.isPresent() && !adminFromDbWithSameMobile.get().getUniqueId().equals(existingAdmin.getUniqueId())) {
            LOG.error("Failed to update loggedInAdmin. Admin with this mobile number already exists - {}", updateAdminDetailRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Admin with this mobile number already exists");
        }
        Admin adminFromDbWithSameEmail = adminRepository.findByEmail(updateAdminDetailRequest.getEmail());
        if (adminFromDbWithSameEmail != null && !adminFromDbWithSameEmail.getUniqueId().equals(existingAdmin.getUniqueId())) {
            LOG.error("Failed to update loggedInAdmin. Admin with this email already exists - {}", updateAdminDetailRequest.getEmail());
            return ResponseUtil.getFailureResponse("Admin with this email already exists");
        }
        if (existingAdmin.isSuperAdmin()) {
            LOG.info("Failed to update admin. Super admin cannot be updated {}", updateAdminDetailRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Super admin cannot be updated.");
        } else {
            LOG.info("Admin with uniqueId {} has been updated successfully", updateAdminDetailRequest.getUniqueId());
            adminMapper.updateAdmin(existingAdmin, updateAdminDetailRequest, request, loggedInAdmin);
            return ResponseUtil.getSuccessfulApiResponse("Admin updated successfully");
        }
    }

    @Override
    public ApiResponse<?> deleteAdmin(DeleteAdminRequest request, Principal principal, HttpServletRequest
            httpServletRequest) {
        Optional<Admin> admin = adminRepository.findByUniqueId(request.getUniqueId());
        if (admin.isEmpty()) {
            LOG.info("Failed to delete admin. Admin not found with uniqueId: {}", request.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        Admin existingAdmin = admin.get();
        if (existingAdmin.isSuperAdmin()) {
            LOG.error("Failed to delete admin. Super admin cannot be deleted : {}", request.getUniqueId());
            return ResponseUtil.getFailureResponse("Super admin cannot be deleted");
        }
        String loggedInAdminUniqueId = existingAdmin.getUniqueId();
        if (Objects.equals(loggedInAdminUniqueId, request.getUniqueId())) {
            LOG.error("Failed to delete admin .Admin cannot delete themself.");
            return ResponseUtil.getFailureResponse("Cannot delete yourself.");
        }
        if (Objects.equals(existingAdmin.getStatus().getName(), StatusConstant.DELETED.getName())) {
            LOG.info("Failed to delete admin. Admin with uniqueId: {} is already deleted", request.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin is already deleted");
        }

        Admin updatedAdmin = adminRepository.save(adminMapper.deleteAdmin(existingAdmin));
        List<AdminToken> activeTokens = adminTokenRepository.findByAdminAndLoggedOutFalse(existingAdmin);
        if (!activeTokens.isEmpty()) {
            LOG.info("Invalidating {} active token(s) for admin with uniqueId: {}", activeTokens.size(), request.getUniqueId());
            for (AdminToken token : activeTokens) {
                AdminTokenUtil.invalidateToken(
                        token.getRefreshToken(),
                        adminTokenRepository::findByRefreshToken,
                        adminTokenRepository
                );
            }
        }
        LOG.info("No active tokens found for admin with uniqueId: {}", request.getUniqueId());

        ActionLogModel actionLogModel = new ActionLogModel();
        actionLogModel.setRemarks(request.getRemarks());
        actionLogModel.setActionType(ActionTypeConstant.DELETE);
        actionLogModel.setTargetType(TargetTypeConstant.ADMIN);
        actionLogModel.setTargetId(updatedAdmin.getId());
        actionLogModel.setIpAddress(httpServletRequest.getRemoteAddr());
        actionLogService.insertActionLog(actionLogModel, principal);
        LOG.info("Admin with uniqueId: {} deleted successfully", request.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Admin deleted successfully");
    }

    @Override
    public ApiResponse<?> viewAdminDetails(ViewAdminDetailRequest viewAdminDetailRequest, Principal loggedIn) {
        Optional<Admin> admin = adminRepository.findByUniqueId(viewAdminDetailRequest.getUniqueId());
        if (admin.isEmpty()) {
            LOG.error("Failed to fetched admin detail. Admin not found {}", viewAdminDetailRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        ViewAdminDetailResponse viewAdminDetailResponse = adminMapper.entityToViewDetails(admin.get());
        LOG.info("Admin detail fetched successfully {}", viewAdminDetailRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponseWithData(viewAdminDetailResponse, "Admin fetched successfully");
    }

    @Override
    public ApiResponse<?> setPassword(SetPasswordRequest setPasswordRequest) {
        Optional<AdminEmailLog> emailLog = adminEmailLogRepository.findByUuid(setPasswordRequest.getUuid());
        if (emailLog.isEmpty()) {
            LOG.error("Failed to set password. The link is invalid - {}", setPasswordRequest.getUuid());
            return ResponseUtil.getFailureResponse("The link is invalid. Please request a new link.");
        }
        if (emailLog.get().getIsExpired()) {
            LOG.error("Failed to set password. The link is expired - {}", setPasswordRequest.getUuid());
            return ResponseUtil.getFailureResponse("The link is expired. Please request a new link.");
        }
        Optional<Admin> adminEntity = emailLog.map(AdminEmailLog::getAdmin);
        if (adminEntity.isEmpty()) {
            LOG.error("Failed to set password. Admin not found for the email log - {}", setPasswordRequest.getUuid());
            return ResponseUtil.getFailureResponse("User not found");
        }
        if (Objects.equals(setPasswordRequest.getPassword(), setPasswordRequest.getConfirmPassword())) {
            Admin admin = adminMapper.setPassword(adminEntity.get(), setPasswordRequest);
            adminRepository.save(admin);
            List<AdminEmailLog> emailLogs = adminEmailLogRepository.findAllByAdminAndIsExpiredFalse(admin);
            for (AdminEmailLog adminEmailLog : emailLogs) {
                adminEmailLog.setIsExpired(true);
            }
            adminEmailLogRepository.saveAll(emailLogs);
            LOG.info("Password set successfully");
            return ResponseUtil.getSuccessfulApiResponse("Password set successfully");
        } else {
            LOG.error("Failed to set password. Passwords do not match - {}", setPasswordRequest.getUuid());
            return ResponseUtil.getFailureResponse("Passwords do not match");
        }
    }

    @Transactional
    @Override
    public ApiResponse<?> editProfile(EditProfileRequest editProfileRequest, MultipartFile
            profilePicture, Principal principal, HttpServletRequest httpServletRequest) throws IOException {
        Admin adminToEditProfile = adminRepository.findByEmail(principal.getName());
        Optional<Admin> adminFromDbWithSameMobile = adminRepository.findByMobileNumber(editProfileRequest.getMobileNumber());
        if (adminFromDbWithSameMobile.isPresent() && !adminFromDbWithSameMobile.get().getMobileNumber().equals(editProfileRequest.getMobileNumber())) {
            LOG.error("Failed to update profile. Admin with this mobile number already exists - {}", editProfileRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Admin with this mobile number already exists");
        }
        Admin editProfile = adminMapper.editProfile(profilePicture, adminToEditProfile, editProfileRequest, principal, httpServletRequest);
        adminRepository.save(editProfile);
        LOG.info("Admin edit profile successfully");
        return ResponseUtil.getSuccessfulApiResponse("Profile edit successfully");
    }

    @Transactional
    @Override
    public ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest, Principal
            principal, HttpServletRequest request) {
        Admin admin = adminRepository.findByEmail(principal.getName());
        if (admin == null) {
            LOG.error("Admin not found with email: {}", principal.getName());
            return ResponseUtil.getFailureResponse("Admin not found.");
        }
        if (!Objects.equals(changePasswordRequest.getPassword(), changePasswordRequest.getConfirmPassword())) {
            LOG.error("Password and confirm password do not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password do not match.");
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), admin.getPassword())) {
            LOG.error("Old password does not match for admin: {}", principal.getName());
            return ResponseUtil.getFailureResponse("Old password does not match.");
        }
        admin.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
//        admin.setPasswordChangeDate(new Date());
//        admin.setUpdatedAt(new Date());
        adminRepository.save(admin);
        LOG.info("Password changed successfully for admin: {}", principal.getName());

        List<AdminToken> activeTokens = adminTokenRepository.findByAdminAndLoggedOutFalse(admin);
        if (!activeTokens.isEmpty()) {
            LOG.info("Invalidating {} active token(s) for admin with email: {}", activeTokens.size(), principal.getName());
            for (AdminToken token : activeTokens) {
                AdminTokenUtil.invalidateToken(
                        token.getRefreshToken(),
                        adminTokenRepository::findByRefreshToken,
                        adminTokenRepository
                );
            }
        }
        actionLogMapper.changePassword(admin.getId(), principal, request);  // Action log for password change
        return ResponseUtil.getSuccessfulApiResponse("Password changed successfully");
    }

    @Override
    public ApiResponse<?> sendPasswordResetLink(SendPasswordResetLinkRequest
                                                        sendPasswordResetLinkRequest, HttpServletRequest request) {
        Admin admin = adminRepository.findByEmail(sendPasswordResetLinkRequest.getEmail());
        if (admin == null) {
            LOG.error("Failed to send password reset link. Admin not found with email: {}", sendPasswordResetLinkRequest.getEmail());
            return ResponseUtil.getFailureResponse("Admin not found.");
        }

        AdminEmailLog adminEmailLog = adminEmailLogMapper.mapToSendPasswordResetLink(admin);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(admin.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.ADMIN_ACCOUNT_VERIFICATION_SUBJECT);
        sendEmailRequest.setMessage(adminEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
        LOG.info("Password reset link sent to admin: {}", sendPasswordResetLinkRequest.getEmail());

        actionLogMapper.resetPassword(admin.getId(), sendPasswordResetLinkRequest.getRemarks(), request.getUserPrincipal(), request);
        return ResponseUtil.getSuccessfulApiResponse("Password reset link sent successfully");
    }

    @Override
    public ApiResponse<?> unblockAdmin(UnblockAdminRequest unblockAdminRequest, HttpServletRequest
            request, Principal loggedIn) {
        Optional<Admin> admin = adminRepository.findByUniqueId(unblockAdminRequest.getUniqueId());
        if (admin.isEmpty()) {
            LOG.error("Failed to unblock admin. Admin not found {}", unblockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        String loggedInAdminUniqueId = loggedIn.getName();


        if (Objects.equals(loggedInAdminUniqueId, unblockAdminRequest.getUniqueId())) {
            LOG.error("Failed to unblock admin .Admin cannot unblock themself.");
            return ResponseUtil.getFailureResponse("Cannot unblock yourself.");
        }
        if (Objects.equals(StatusConstant.ACTIVE.getName(), admin.get().getStatus().getName())) {
            LOG.error("Failed to unblock admin. Admin is active {}", unblockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Admin is already in active state");
        }
        if (Objects.equals(StatusConstant.PENDING.getName(), admin.get().getStatus().getName())) {
            LOG.error("Failed to unblock admin. Pending admin cannot be unblocked {}", unblockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Pending admin cannot be unblocked");
        }
        if (Objects.equals(StatusConstant.DELETED.getName(), admin.get().getStatus().getName())) {
            LOG.error("Failed to unblock admin. Deleted admin cannot be unblocked {}", unblockAdminRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Deleted admin cannot be unblocked");
        }

        adminMapper.unblockAdmin(admin.get(), unblockAdminRequest, request, loggedIn);
        LOG.info("Admin unblocked successfully - {}", unblockAdminRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("Admin unblocked successfully");
    }

    @Override
    public ApiResponse<?> viewProfile(Principal loggedInAdmin) {
        Admin admin = adminRepository.findByEmail(loggedInAdmin.getName());
        if (admin == null) {
            LOG.error("Failed to view profile. Admin not found: {}", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        ViewProfileResponse viewProfileResponse = adminMapper.viewAdmin(admin);
        LOG.info("Profile viewed successfully - {}", viewProfileResponse.getName());
        return ResponseUtil.getSuccessfulApiResponseWithData(viewProfileResponse, "Profile viewed successfully");

    }
}
