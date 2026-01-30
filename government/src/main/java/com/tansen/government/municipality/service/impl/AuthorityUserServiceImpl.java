package com.tansen.government.municipality.service.impl;

import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.*;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.government.core.util.AuthorityTokenUtil;
import com.tansen.government.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.government.municipality.dto.*;
import com.tansen.government.municipality.mapper.MunicipalityEmailMapper;
import com.tansen.government.municipality.mapper.MunicipalityForgetPasswordOptMapper;
import com.tansen.government.municipality.mapper.MunicipalityUserMapper;
import com.tansen.government.municipality.service.AuthorityUserService;
import com.tansen.repository.AuthorityUserEmailLogRepository;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.AuthorityUserTokenRepository;
import com.tansen.repository.ForgotPasswordOtpRepository;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthorityUserServiceImpl implements AuthorityUserService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityUserServiceImpl.class);

    private final AuthorityUserRepository authorityUserRepository;
    private final AuthorityUserTokenRepository authorityUserTokenRepository;
    private final AuthorityUserSearchRepository authorityUserSearchRepository;
    private final SearchResponse searchResponse;
    private final MunicipalityEmailMapper municipalityEmailMapper;
    private final ForgotPasswordOtpRepository forgotPasswordOtpRepository;
    private final MunicipalityForgetPasswordOptMapper municipalityForgetPasswordOptMapper;
    private final AuthorityUserEmailLogRepository authorityUserEmailLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityUserEmailLogMapper authorityUserEmailLogMapper;
    private final MailService mailService;
    private final MunicipalityUserMapper   municipalityUserMapper;

    public AuthorityUserServiceImpl( MunicipalityUserMapper   municipalityUserMapper,AuthorityUserRepository vendorUserRepository, AuthorityUserTokenRepository vendorUserTokenRepository, AuthorityUserSearchRepository vendorUserSearchRepository, SearchResponse searchResponse, MunicipalityEmailMapper vendorEmailMapper, ForgotPasswordOtpRepository forgotPasswordOtpRepository, MunicipalityForgetPasswordOptMapper vendorForgetPasswordOptMapper,
                                    AuthorityUserEmailLogRepository vendorUserEmailLogRepository, PasswordEncoder passwordEncoder, AuthorityUserEmailLogMapper vendorUserEmailLogMapper, MailService mailService) {
        this.authorityUserRepository = vendorUserRepository;
        this.municipalityUserMapper = municipalityUserMapper;
        this.authorityUserTokenRepository = vendorUserTokenRepository;
        this.authorityUserSearchRepository = vendorUserSearchRepository;
        this.searchResponse = searchResponse;
        this.municipalityEmailMapper = vendorEmailMapper;
        this.forgotPasswordOtpRepository = forgotPasswordOtpRepository;
        this.municipalityForgetPasswordOptMapper = vendorForgetPasswordOptMapper;
        this.authorityUserEmailLogRepository = vendorUserEmailLogRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityUserEmailLogMapper = vendorUserEmailLogMapper;
        this.mailService = mailService;
    }


    @Override
    public ApiResponse<?> listVendorUsers(SearchParam searchParam, Principal loggedInUser) {
        if (findAuthorityUser(searchParam, loggedInUser))
            return ResponseUtil.getFailureResponse("Logged in User Not Found.");

        SearchResponseWithMapperBuilder<AuthorityUser, ListMunicipalityUserResponse> responseBuilder =
                SearchResponseWithMapperBuilder.<AuthorityUser, ListMunicipalityUserResponse>builder()
                        .count(authorityUserSearchRepository::count)
                        .searchData(authorityUserSearchRepository::getAll)
                        .mapperFunction(this.municipalityUserMapper::listAllVendors)
                        .searchParam(searchParam)
                        .build();

        PageableResponse<ListMunicipalityUserResponse> response = searchResponse.getSearchResponse(responseBuilder);

        LOG.info("Authority User listed successfully");
        return ResponseUtil.getSuccessfulApiResponse(response, "Authority User listed Successfully");
    }

    private boolean findAuthorityUser(SearchParam searchParam, Principal loggedInUser) {
        Optional<AuthorityUser> byEmail = authorityUserRepository.findByEmail(loggedInUser.getName());
        if (byEmail.isEmpty()) {
            LOG.error("Failed to find municipality Users by email {}", loggedInUser.getName());
            return true;
        }

        Municipality municipality = byEmail.get().getMunicipality();

        if (municipality != null) {
            searchParam.getParam().put("municipality", municipality.getGovernmentName());
            searchParam.getParam().put("municipalityUniqueId", municipality.getUniqueId());
        }
        return false;
    }

    @Override
    public ApiResponse<?> createVendorUser(CreateMunicipalityUserRequest createVendorUserRequest, Principal loggedInVendor, HttpServletRequest request) {
        Optional<AuthorityUser> vendorUserOptional = authorityUserRepository.findByEmail(loggedInVendor.getName());
        if (vendorUserOptional.isEmpty()) {
            LOG.error("Vendor user with email {} does not exist", loggedInVendor.getName());
            return ResponseUtil.getFailureResponse("Vendor with this email does not exist");
        }
        Municipality vendor = vendorUserOptional.get().getMunicipality();
        Optional<AuthorityUser> existingVendorUserByPhone = authorityUserRepository.findByPhoneNumber(createVendorUserRequest.getMobileNumber());
        if (existingVendorUserByPhone.isPresent()) {
            LOG.error("Vendor user with mobileNumber {} already exists", createVendorUserRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Vendor user with this mobile number already exists");
        }
        Optional<AuthorityUser> existingVendorUserByEmail = authorityUserRepository.findByEmail(createVendorUserRequest.getEmail());
        if (existingVendorUserByEmail.isPresent()) {
            LOG.error("User with email {} already exists", createVendorUserRequest.getEmail());
            return ResponseUtil.getFailureResponse("User with this email already exists");
        }
        AuthorityUser newVendorUser = municipalityUserMapper.createVendor(createVendorUserRequest, vendor, request, loggedInVendor);
        authorityUserRepository.save(newVendorUser);

        AuthorityUserEmailLog vendorEmailLog = authorityUserEmailLogMapper.mapToVendor(newVendorUser);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(newVendorUser.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.AUTHORITY_USER_ACCOUNT_VERIFICATION_SUBJECT);
        sendEmailRequest.setMessage(vendorEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
        return ResponseUtil.getSuccessfulApiResponse("User created successfully");
    }

    @Transactional
    @Override
    public ApiResponse<?> updateVendorUser(UpdateMunicipalityUserRequest updateVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request) {
        Optional<AuthorityUser> superUser = authorityUserRepository.findByEmail(loggedInAdmin.getName());
        if (superUser.isEmpty()) {
            LOG.error("User with uniqueId {} does not exist", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("User with unique id does not exist");
        }
        AuthorityUser user = superUser.get();
        Municipality vendor = user.getMunicipality();
        Optional<AuthorityUser> targetedVendor = authorityUserRepository.findByUniqueId(updateVendorUserRequest.getUniqueId());
        if (targetedVendor.isEmpty()) {
            LOG.error("User with uniqueId {} does not exist", updateVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId does not exist");
        }
        AuthorityUser existingVendor = targetedVendor.get();

        if (!Objects.equals(existingVendor.getMunicipality(), vendor)) {
            LOG.error("User with uniqueId {} is not from same vendor", updateVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId is not from same vendor");
        }

        if (existingVendor.getStatus().getName().equals(StatusConstant.DELETED.getName())) {
            LOG.info("Failed to update vendor. Vendor with uniqueId {} is deleted", updateVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Deleted vendor cannot be updated");
        }
        if (existingVendor.getStatus().getName().equals(StatusConstant.BLOCKED.getName())) {
            LOG.info("Failed to update vendor. Vendor with uniqueId {} is blocked", updateVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Blocked vendor cannot be updated");
        }

        Optional<AuthorityUser> adminFromDbWithSameMobile = authorityUserRepository.findByPhoneNumber(updateVendorUserRequest.getMobileNumber());
        if (adminFromDbWithSameMobile.isPresent() && !adminFromDbWithSameMobile.get().getUniqueId().equals(existingVendor.getUniqueId())) {
            LOG.error("Failed to update loggedInAdmin. Vendor with this mobile number already exists - {}", updateVendorUserRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("Vendor with this mobile number already exists");
        }
        Optional<AuthorityUser> vendorFromDbWithSameEmail = authorityUserRepository.findByEmail(updateVendorUserRequest.getEmail());
        if (vendorFromDbWithSameEmail.isEmpty()) {
            LOG.error("Vendor with this email does not exist");
            return ResponseUtil.getFailureResponse("Vendor with this email does not exist");
        }
        AuthorityUser vendorUser = vendorFromDbWithSameEmail.get();
        if (!vendorUser.getUniqueId().equals(existingVendor.getUniqueId())) {
            LOG.error("Failed to update vendor. Vendor with this email already exists - {}", updateVendorUserRequest.getEmail());
            return ResponseUtil.getFailureResponse("Vendor with this email already exists");
        }

        if (!Objects.equals(updateVendorUserRequest.getEmail(), existingVendor.getEmail())) {
            handleToken(existingVendor, updateVendorUserRequest.getUniqueId());
        }
        if (existingVendor.isAuthorityAdmin()) {
            LOG.info("Failed to update admin. Super vendor cannot be updated {}", updateVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Super vendor cannot be updated.");
        } else {
            LOG.info("User with uniqueId {} has been updated successfully", updateVendorUserRequest.getUniqueId());
            authorityUserRepository.save(municipalityUserMapper.updateVendorUser(existingVendor, updateVendorUserRequest, request, loggedInAdmin));
            return ResponseUtil.getSuccessfulApiResponse("Vendor updated successfully");
        }
    }

    @Override
    public ApiResponse<?> viewProfile(Principal loggedInVendor) {
        Optional<AuthorityUser> user = authorityUserRepository.findByEmail(loggedInVendor.getName());
        if (user.isEmpty()) {
            LOG.error("Failed to view profile. Admin not found: {}", loggedInVendor.getName());
            return ResponseUtil.getFailureResponse("Admin not found");
        }
        ViewProfileResponse viewProfileResponse = municipalityUserMapper.viewProfile(user.get());
        LOG.info("Profile viewed successfully - {}", viewProfileResponse.getFullName());
        return ResponseUtil.getSuccessfulApiResponseWithData(viewProfileResponse, "Profile viewed successfully");
    }

    @Transactional
    @Override
    public ApiResponse<?> blockVendorUser(BlockMunicipalityUserRequest blockVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request) {
        Optional<AuthorityUser> superUser = authorityUserRepository.findByEmail(loggedInAdmin.getName());
        if (superUser.isEmpty()) {
            LOG.error("Vendor user with uniqueId {} does not exist", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Vendor user with unique id does not exist");
        }
        AuthorityUser user = superUser.get();
        Municipality vendor = user.getMunicipality();
        Optional<AuthorityUser> targetedVendor = authorityUserRepository.findByUniqueId(blockVendorUserRequest.getUniqueId());
        if (targetedVendor.isEmpty()) {
            LOG.error("Vendor user with uniqueId {} does not exist", blockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId does not exist");
        }
        AuthorityUser existingVendor = targetedVendor.get();
        if (!Objects.equals(existingVendor.getMunicipality(), vendor)) {
            LOG.error("Vendor user with uniqueId {} is not from same vendor", blockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId is not from same vendor");
        }

        if (existingVendor.getStatus().getName().equals(StatusConstant.DELETED.getName())) {
            LOG.info("Failed to block vendor. Vendor with uniqueId {} is deleted", blockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Deleted vendor cannot be block.");
        }
        if (existingVendor.getStatus().getName().equals(StatusConstant.BLOCKED.getName())) {
            LOG.info("Failed to block vendor. Vendor with uniqueId {} is deleted", blockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Blocked vendor cannot be block.");
        }
        if (existingVendor.getStatus().getName().equals(StatusConstant.ACTIVE.getName())) {
            if (existingVendor.isAuthorityAdmin()) {
                LOG.info("Failed to block vendor. Super vendor cannot be blocked {}", blockVendorUserRequest.getUniqueId());
                return ResponseUtil.getFailureResponse("Super vendor cannot be blocked.");
            } else {
                handleToken(existingVendor, blockVendorUserRequest.getUniqueId());
                authorityUserRepository.save(municipalityUserMapper.blockVendorUser(existingVendor, blockVendorUserRequest, request, loggedInAdmin));
                LOG.info("User with uniqueId {} has been blocked successfully", blockVendorUserRequest.getUniqueId());
                return ResponseUtil.getSuccessfulApiResponse("Vendor block  successfully");
            }

        }
        return ResponseUtil.getSuccessfulApiResponse("Cannot block vendor ");
    }

    @Override
    public ApiResponse<?> setPassword(SetPasswordRequest setPasswordRequest) {
        if (Objects.equals(setPasswordRequest.getPassword(), setPasswordRequest.getConfirmPassword())) {
            Optional<AuthorityUserEmailLog> emailLog = authorityUserEmailLogRepository.findByUniqueId(setPasswordRequest.getUuid());
            if (emailLog.isEmpty()) {
                LOG.error("Failed to set password. The link is invalid - {}", setPasswordRequest.getUuid());
                return ResponseUtil.getFailureResponse("The link is invalid. Please request a new link.");
            }
            if (emailLog.get().getIsExpired()) {
                LOG.error("Failed to set password. The link is expired - {}", setPasswordRequest.getUuid());
                return ResponseUtil.getFailureResponse("The link is expired. Please request a new link.");
            }
            Optional<AuthorityUser> vendorEntity = emailLog.map(AuthorityUserEmailLog::getAuthorityUser);
            if (vendorEntity.isEmpty()) {
                LOG.error("Failed to set password. Admin not found for the email log - {}", setPasswordRequest.getUuid());
                return ResponseUtil.getFailureResponse("User not found");

            }

            AuthorityUser user = municipalityUserMapper.setPassword(vendorEntity.get(), setPasswordRequest);
            authorityUserRepository.save(user);
            List<AuthorityUserEmailLog> emailLogs = authorityUserEmailLogRepository.findAllByAuthorityUserAndIsExpiredFalse(user);
            for (AuthorityUserEmailLog vendorUserEmailLog : emailLogs) {
                vendorUserEmailLog.setIsExpired(true);
            }
            authorityUserEmailLogRepository.saveAll(emailLogs);
            LOG.info("Password set successfully");
            return ResponseUtil.getSuccessfulApiResponse("Password set successfully");
        } else {
            LOG.error("Failed to set password. Passwords do not match - {}", setPasswordRequest.getUuid());
            return ResponseUtil.getFailureResponse("Passwords do not match");
        }
    }


    @Transactional
    @Override
    public ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest, Principal principal, HttpServletRequest request) {
        Optional<AuthorityUser> vendorUsers = authorityUserRepository.findByEmail(principal.getName());
        if (vendorUsers.isEmpty()) {
            LOG.error("User not found with email: {}", principal.getName());
            return ResponseUtil.getFailureResponse("Admin not found.");
        }
        if (!Objects.equals(changePasswordRequest.getPassword(), changePasswordRequest.getConfirmPassword())) {
            LOG.error("Password and confirm password do not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password do not match.");
        }
        AuthorityUser user = vendorUsers.get();
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            LOG.error("Old password does not match for admin: {}", principal.getName());
            return ResponseUtil.getFailureResponse("Old password does not match.");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        user.setPasswordChangeDate(LocalDateTime.now());
        authorityUserRepository.save(user);
        LOG.info("Password changed successfully for admin: {}", principal.getName());
        handleToken(user, user.getUniqueId());
        municipalityUserMapper.changePassword(user.getId(), principal, request);  // Action log for password change
        return ResponseUtil.getSuccessfulApiResponse("Password changed successfully");
    }


    @Override
    public ApiResponse<?> sendPasswordResetLink(SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request) {
        Optional<AuthorityUser> user = authorityUserRepository.findByEmail(sendPasswordResetLinkRequest.getEmail());
        if (user.isEmpty()) {
            LOG.error("Failed to send password reset link. User not found with email: {}", sendPasswordResetLinkRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found.");
        }

        AuthorityUser vendorUsers = municipalityUserMapper.sendPasswordResetLink(user.get(), sendPasswordResetLinkRequest, request, request.getUserPrincipal());
        authorityUserRepository.save(vendorUsers);


        AuthorityUserEmailLog vendorEmailLog = authorityUserEmailLogMapper.mapToSendPasswordResetLink(vendorUsers);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(vendorUsers.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.USER_ACCOUNT_RESET_PASSWORD_SUBJECT);
        sendEmailRequest.setMessage(vendorEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
        LOG.info("Password reset link sent to vendor: {}", sendPasswordResetLinkRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Password reset link sent successfully");
    }

    @Override
    public ApiResponse<?> editProfile(EditProfileRequest editProfileRequest, Principal principal, HttpServletRequest httpServletRequest) {
        Optional<AuthorityUser> vendorToEditProfile = authorityUserRepository.findByEmail(principal.getName());
        Optional<AuthorityUser> adminFromDbWithSameMobile = authorityUserRepository.findByPhoneNumber(editProfileRequest.getMobileNumber());
        if (adminFromDbWithSameMobile.isPresent() && !adminFromDbWithSameMobile.get().getPhoneNumber().equals(editProfileRequest.getMobileNumber())) {
            LOG.error("Failed to update profile. User with this mobile number already exists - {}", editProfileRequest.getMobileNumber());
            return ResponseUtil.getFailureResponse("User with this mobile number already exists");
        }
        AuthorityUser editProfile = municipalityUserMapper.editProfile(vendorToEditProfile.get(), editProfileRequest, principal, httpServletRequest);
        authorityUserRepository.save(editProfile);
        LOG.info("User edit profile successfully");
        return ResponseUtil.getSuccessfulApiResponse("Profile edit successfully");
    }

    @Override
    public ApiResponse<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        Optional<AuthorityUser> vendorUsers = authorityUserRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (vendorUsers.isEmpty()) {
            LOG.error("Failed to forgot password. Vendor user not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Vendor user  not found with the provided email.");
        }
        invalidExistingOtpAndSendNewOtpEmail(forgotPasswordRequest, vendorUsers.get());
        return ResponseUtil.getSuccessfulApiResponse("OTP sent successfully to your email. Please check your inbox.");
    }

    @Override
    public ApiResponse<?> verifyForgotPasswordOtp(VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        Optional<AuthorityUser> vendorUsers = authorityUserRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (vendorUsers.isEmpty()) {
            LOG.error("Failed to verify otp. Vendor User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Vendor User not found with the provided email.");
        }
        ForgotPasswordOtp validOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndOtpAndIsValidTrue(vendorUsers.get().getUniqueId(), forgotPasswordRequest.getOtp());
        if (validOtp == null) {
            LOG.error("Failed to verify otp. Invalid OTP for vendor user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Invalid OTP. Please try again.");
        }
        if (validOtp.getValidDate().isBefore(Instant.now())){
            validOtp.setIsValid(false);
            forgotPasswordOtpRepository.save(validOtp);
            LOG.error("Failed to verify otp. OTP expired for vendor user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("OTP has expired. Please request a new one.");
        }
        LOG.info("OTP verified successfully for vendor user: {}", forgotPasswordRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("OTP verified successfully.");
    }

    @Override
    public ApiResponse<?> resendForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        Optional<AuthorityUser> vendorUsers = authorityUserRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (vendorUsers.isEmpty()) {
            LOG.error("Failed to resend otp. Vendor user not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Vendor user not found with the provided email.");
        }
        invalidExistingOtpAndSendNewOtpEmail(forgotPasswordRequest, vendorUsers.get());
        return ResponseUtil.getSuccessfulApiResponse("OTP resend successfully to your email. Please check your inbox.");
    }

    @Override
    public ApiResponse<?> setForgetPassword(SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        if (!forgotPasswordRequest.getPassword().equals(forgotPasswordRequest.getConfirmPassword())) {
            LOG.error("Failed to set forget password. Password and confirm password does not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password does not match.");
        }
        Optional<AuthorityUser> vendorUsers = authorityUserRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (vendorUsers.isEmpty()) {
            LOG.error("Failed to set forget password. User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        ForgotPasswordOtp validUsrOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndOtpAndIsValidTrue(vendorUsers.get().getUniqueId(), forgotPasswordRequest.getOtp());
        if (validUsrOtp == null) {
            LOG.error("Failed to set forget password. Invalid OTP for user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Invalid OTP. Please try again.");
        }
        municipalityUserMapper.forgotPasswordChange(vendorUsers.get(), validUsrOtp, forgotPasswordRequest);
        LOG.info("Vendor User password changed successfully for user: {}", forgotPasswordRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Password changed successfully.");
    }

    private void handleToken(AuthorityUser existingVendor, String uniqueId) {
        List<AuthorityUserToken> activeTokens = authorityUserTokenRepository.findByAuthorityUserAndLoggedOutFalse(existingVendor);
        if (!activeTokens.isEmpty()) {
            LOG.info("Invalidating {} active token(s) for vendor with uniqueId: {}", activeTokens.size(), uniqueId);
            for (AuthorityUserToken token : activeTokens) {
                AuthorityTokenUtil.invalidateToken(
                        token.getRefreshToken(),
                        authorityUserTokenRepository::findByRefreshToken,
                        authorityUserTokenRepository
                );
            }
        } else {
            LOG.info("No active tokens found for Vendor  user  with uniqueId: {}", uniqueId);
        }
    }


    @Override
    public ApiResponse<?> deleteVendorUser(DeleteMunicipalityUserRequest deleteVendorUserRequest, HttpServletRequest request, Principal loggedInAdmin) {
        Optional<AuthorityUser> superUser = authorityUserRepository.findByEmail(loggedInAdmin.getName());
        if (superUser.isEmpty()) {
            LOG.error("Failed to delete vendor user. Vendor user with email {} does not exist (Logged in user).", loggedInAdmin.getName());
            return ResponseUtil.getFailureResponse("Vendor user with email does not exist");
        }
        AuthorityUser user = superUser.get();
//        LOG.info("Delete user {} successfully", user.getEmail(),);
        Municipality vendor = user.getMunicipality();
        Optional<AuthorityUser> targetedVendor = authorityUserRepository.findByUniqueId(deleteVendorUserRequest.getUniqueId());
        if (targetedVendor.isEmpty()) {
            LOG.error("Failed to delete vendor user. Vendor user with uniqueId {} does not exist", deleteVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId does not exist");
        }
        AuthorityUser existingVendor = targetedVendor.get();

        if (!Objects.equals(existingVendor.getMunicipality(), vendor)) {
            LOG.error("Failed to delete vendor user. Vendor user with uniqueId {} is not from same vendor", deleteVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId is not from same vendor");
        }
        if (Objects.equals(existingVendor.getStatus().getName(), StatusConstant.DELETED.getName())) {
            LOG.info("the user with uniqueId {} has already been deleted", deleteVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("The user with uniqueId is already been deleted");
        }
        if (existingVendor.isAuthorityAdmin()) {
            return ResponseUtil.getFailureResponse("Super vendor cannot be deleted");
        }

        List<AuthorityUserToken> activeTokens = authorityUserTokenRepository.findByAuthorityUserAndLoggedOutFalse(existingVendor);
        if (activeTokens != null && !activeTokens.isEmpty()) {
            for (AuthorityUserToken token : activeTokens) {
                AuthorityTokenUtil.invalidateToken(
                        token.getRefreshToken(),
                        authorityUserTokenRepository::findByRefreshToken,
                        authorityUserTokenRepository
                );
            }
        } else {
            LOG.info("No active tokens found for admin with uniqueId: {}", deleteVendorUserRequest.getUniqueId());
        }

        LOG.info("vendor user id deleted successfully with the id {}", deleteVendorUserRequest.getUniqueId());
        AuthorityUser vendorUsers = municipalityUserMapper.deleteVendorUser(existingVendor, deleteVendorUserRequest, request, loggedInAdmin);
        authorityUserRepository.save(vendorUsers);
        return ResponseUtil.getSuccessfulApiResponse("Vendor User deleted successfully");

    }

    @Override
    public ApiResponse<?> unblockVendorUser(UnblockMunicipalityUserRequest unBlockVendorUserRequest, Principal loggedInVendor, HttpServletRequest request) {
        Optional<AuthorityUser> superUser = authorityUserRepository.findByUniqueId(unBlockVendorUserRequest.getUniqueId());
        if (superUser.isEmpty()) {
            LOG.error("Failed to unblock vendor user. Vendor user with uniqueId {} does not exist", loggedInVendor.getName());
            return ResponseUtil.getFailureResponse("Vendor user with uniqueId does not exist");
        }
        if (Objects.equals(superUser.get().getStatus().getName(), StatusConstant.DELETED.getName())) {
            LOG.info("Fail to unblock vendor user. Deleted vendor user cannot be unblocked {}", unBlockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("The user with uniqueId is already deleted");
        }
        if (Objects.equals(superUser.get().getStatus().getName(), StatusConstant.ACTIVE.getName())) {
            LOG.info("Fail to unblock the vendor user. Vendor user is already Active {}", unBlockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("The user with uniqueId is already Active");
        }
        if (Objects.equals(superUser.get().getStatus().getName(), StatusConstant.PENDING.getName())) {
            LOG.info("Fail to unblock the vendor user. Pending  vendor user cannot be unblock{}", unBlockVendorUserRequest.getUniqueId());
            return ResponseUtil.getFailureResponse("The user with uniqueId is already Pending");
        }
        AuthorityUser vendorUsers = municipalityUserMapper.unblockVendorUser(superUser.get(), unBlockVendorUserRequest, loggedInVendor, request);
        authorityUserRepository.save(vendorUsers);
        LOG.info("Vendor user is unblocked successfully with the id {}", unBlockVendorUserRequest.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponse("User has been unblocked successfully");
    }

    @Transactional
    @Override
    public ApiResponse<?> ViewUsersDetail(ViewUsersDetail viewUsersDetail, Principal principal) {
        Optional<AuthorityUser> vendorUser = authorityUserRepository.findByUniqueId(viewUsersDetail.getUniqueId());
        if (vendorUser.isEmpty()) {
            LOG.error("Failed to fetched user details. User not found {}", principal.getName());
            return ResponseUtil.getFailureResponse("User not found");
        }
        ViewUserDetailResponse viewUserDetailResponse = municipalityUserMapper.entityToViewDetails(vendorUser.get());
        LOG.info("user detail fetched successfully {}", viewUserDetailResponse.getUniqueId());
        return ResponseUtil.getSuccessfulApiResponseWithData(viewUserDetailResponse, "User detail fetched successfully");
    }

    private void invalidExistingOtpAndSendNewOtpEmail(ForgotPasswordRequest forgotPasswordRequest, AuthorityUser vendorUsers) {
        List<ForgotPasswordOtp> validOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndIsValidTrue(vendorUsers.getUniqueId());
        if (!validOtp.isEmpty()) {
            validOtp.forEach(otp -> otp.setIsValid(false));
            forgotPasswordOtpRepository.saveAll(validOtp);
        }
        ForgotPasswordOtp forgotPasswordOtp = municipalityForgetPasswordOptMapper.mapToEntity(forgotPasswordRequest, vendorUsers);
        ForgotPasswordOtp savedOtp = forgotPasswordOtpRepository.save(forgotPasswordOtp);

        AuthorityUserEmailLog vendorEmailLog = municipalityEmailMapper.mapOtpSendRequest(vendorUsers, savedOtp);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(vendorUsers.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.USER_FORGOT_PASSWORD);
        sendEmailRequest.setMessage(vendorEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
    }
}
