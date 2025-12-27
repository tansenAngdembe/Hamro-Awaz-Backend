package com.tansen.app.service.impl;


import com.tansen.app.core.util.UserTokenUtil;
import com.tansen.app.dto.request.*;
import com.tansen.app.dto.response.UserProfileResponse;
import com.tansen.app.mapper.EmailMapper;
import com.tansen.app.mapper.ForgotPasswordOptMapper;
import com.tansen.app.mapper.UserMapper;
import com.tansen.app.service.UserService;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.dto.response.UserResponse;
import com.tansen.common.service.MailService;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.*;
import com.tansen.repository.ForgotPasswordOtpRepository;
import com.tansen.repository.UserRegistrationEmailLogRepository;
import com.tansen.repository.UserRepository;
import com.tansen.repository.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SearchResponse searchResponse;
    private final ForgotPasswordOtpRepository forgotPasswordOtpRepository;
    private final EmailMapper emailMapper;
    private final ForgotPasswordOptMapper forgotPasswordOptMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository userTokenRepository;
    private final UserRegistrationEmailLogRepository userRegistrationEmailLogRepository;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,UserRegistrationEmailLogRepository userRegistrationEmailLogRepository, SearchResponse searchResponse, ForgotPasswordOptMapper forgotPasswordOptMapper, ForgotPasswordOtpRepository forgotPasswordOtpRepository,  MailService mailService, PasswordEncoder passwordEncoder, UserTokenRepository userTokenRepository, EmailMapper emailMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.searchResponse = searchResponse;
        this.forgotPasswordOtpRepository = forgotPasswordOtpRepository;
        this.emailMapper = emailMapper;
        this.forgotPasswordOptMapper = forgotPasswordOptMapper;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.userTokenRepository = userTokenRepository;
        this.userRegistrationEmailLogRepository = userRegistrationEmailLogRepository;
    }


    @Override
    @Transactional
    public ApiResponse<?> createUser(CreateUserRequest request, HttpServletRequest httpServletRequest) {
        if (!request.getPassword().equals(request.getConfirmPassword())){
            LOG.error("Failed to create user. Password and confirm password does not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password does not match.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            LOG.error("Failed to create user. Email already exists: {}", request.getEmail());
            return ResponseUtil.getFailureResponse("Email already exists. Please use a different email.");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            LOG.error("Failed to create user. Phone number already exists: {}", request.getPhoneNumber());
            return ResponseUtil.getFailureResponse("Phone number already exists. Please use a different phone number.");
        }
        User user = userMapper.mapToEntity(request, passwordEncoder, httpServletRequest);
        userRepository.save(user);
        return ResponseUtil.getSuccessfulApiResponse("User created successfully. Please check your email for verification OTP.");
    }

    @Override
    @Transactional
    public ApiResponse<?> sendAccountActivationEmail(SendAccountActivationEmailRequest request) {
        User byEmail = userRepository.findByEmail(request.getEmail());
        if (byEmail == null) {
            LOG.error("Failed to send account activation email. Email not found: {}", request.getEmail());
            return ResponseUtil.getFailureResponse("User not found with this email.");
        }
        if (!Objects.equals(byEmail.getStatus().getName(), StatusConstant.PENDING.getName())) {
            return ResponseUtil.getFailureResponse("Your account has already been Activated.");
        }
        userMapper.sendAccountResetEmail(byEmail);
        return ResponseUtil.getSuccessfulApiResponse("Email has been sent successfully.");
    }

    @Override
    public ApiResponse<?> verifyUserAccount(VerifyUserAccountRequest request, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            LOG.error("Failed to verify user account. User not found with email: {}", request.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        UserRegistrationEmailLog validUserOtp = userRegistrationEmailLogRepository.findByUserAndOtpAndIsOtpExpiredFalse(user, request.getOtp());
        if (validUserOtp == null) {
            LOG.error("Failed to verify user account. Invalid OTP for user: {}", request.getEmail());
            return ResponseUtil.getFailureResponse("Invalid OTP. Please try again.");
        }
        if (validUserOtp.getExpirationTime().isBefore(LocalDateTime.now())) {
            validUserOtp.setIsOtpExpired(true);
            userRegistrationEmailLogRepository.save(validUserOtp);
            LOG.error("Failed to verify user account. OTP expired for user: {}", request.getEmail());
            return ResponseUtil.getFailureResponse("OTP has expired. Please request a new one.");
        }
        userMapper.verifyUserRegistrationOtp(user, validUserOtp);
        LOG.info("User account verified successfully for user: {}", request.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("User account verified successfully.");
    }

    @Override
    @Transactional
    public ApiResponse<?> updateProfile(UserProfileUpdateRequest userProfileUpdateRequest, MultipartFile profilePicture, Principal connectedUser, HttpServletRequest request) {
        User user = userRepository.findByEmail(connectedUser.getName());
        try {
            User editProfile = userMapper.updateProfile(profilePicture, user, userProfileUpdateRequest);
            userRepository.save(editProfile);
            LOG.info("User Profile Updated Successfully");
            return ResponseUtil.getSuccessfulApiResponse("Successfully updated profile picture.");
        } catch (IOException e) {
            LOG.info("User Profile Updated Failed");
            return ResponseUtil.getFailureResponse("Error uploading profile picture.");
        }
    }

    @Override
    public ApiResponse<?> viewProfilePhoto(HttpServletRequest request, Principal connectedUser) {
        User user = userRepository.findByEmail(connectedUser.getName());
        if (user == null) {
            return ResponseUtil.getFailureResponse("Please login again.");
        }
        UserProfileResponse userProfileResponse = userMapper.userProfilePhotoResponse(user);
        LOG.info("User can view profile picture.");
        return ResponseUtil.getSuccessfulApiResponse(userProfileResponse, "Successfully view profile picture.");
    }

    @Override
    public ApiResponse<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (user == null) {
            LOG.error("Failed to forgot password. User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        invalidExistingOtpAndSendNewOtpEmail(forgotPasswordRequest, user);
        return ResponseUtil.getSuccessfulApiResponse("OTP sent successfully to your email. Please check your inbox.");
    }

    @Override
    public ApiResponse<?> verifyForgotPasswordOtp(VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (user == null) {
            LOG.error("Failed to verify otp. User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        ForgotPasswordOtp validOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndOtpAndIsValidTrue(user.getUniqueId(), forgotPasswordRequest.getOtp());
        if (validOtp == null) {
            LOG.error("Failed to verify otp. Invalid OTP for user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Invalid OTP. Please try again.");
        }
        if (validOtp.getValidDate().isBefore(Instant.now())){
            validOtp.setIsValid(false);
            forgotPasswordOtpRepository.save(validOtp);
            LOG.error("Failed to verify otp. OTP expired for user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("OTP has expired. Please request a new one.");
        }
        LOG.info("OTP verified successfully for user: {}", forgotPasswordRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("OTP verified successfully.");
    }

    @Override
    public ApiResponse<?> resendForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (user == null) {
            LOG.error("Failed to resend otp. User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        invalidExistingOtpAndSendNewOtpEmail(forgotPasswordRequest, user);
        return ResponseUtil.getSuccessfulApiResponse("OTP resend successfully to your email. Please check your inbox.");
    }

    @Override
    public ApiResponse<?> setForgetPassword(SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        if (!forgotPasswordRequest.getPassword().equals(forgotPasswordRequest.getConfirmPassword())) {
            LOG.error("Failed to set forget password. Password and confirm password does not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password does not match.");
        }
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail());
        if (user == null) {
            LOG.error("Failed to set forget password. User not found with email: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with the provided email.");
        }
        ForgotPasswordOtp validUsrOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndOtpAndIsValidTrue(user.getUniqueId(), forgotPasswordRequest.getOtp());
        if (validUsrOtp == null) {
            LOG.error("Failed to set forget password. Invalid OTP for user: {}", forgotPasswordRequest.getEmail());
            return ResponseUtil.getFailureResponse("Invalid OTP. Please try again.");
        }
        userMapper.forgotPasswordChange(user, validUsrOtp, forgotPasswordRequest);
        LOG.info("User password changed successfully for user: {}", forgotPasswordRequest.getEmail());
        return ResponseUtil.getSuccessfulApiResponse("Password changed successfully.");
    }


    private void invalidExistingOtpAndSendNewOtpEmail(ForgotPasswordRequest forgotPasswordRequest, User user) {
        List<ForgotPasswordOtp> validOtp = forgotPasswordOtpRepository.findByUserUniqueIdAndIsValidTrue(user.getUniqueId());
        if (!validOtp.isEmpty()){
            validOtp.forEach(otp -> otp.setIsValid(false));
            forgotPasswordOtpRepository.saveAll(validOtp);
        }
        ForgotPasswordOtp forgotPasswordOtp = forgotPasswordOptMapper.mapToEntity(forgotPasswordRequest, user);
        ForgotPasswordOtp savedOtp = forgotPasswordOtpRepository.save(forgotPasswordOtp);

        UserEmailLog userEmailLog = emailMapper.mapOtpSendRequest(user, savedOtp);

        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(user.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.USER_FORGOT_PASSWORD);
        sendEmailRequest.setMessage(userEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);
    }
    @Override
    public ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser, HttpServletRequest request) {
        User user= userRepository.findByEmail(connectedUser.getName());
        if(user==null){
            LOG.info("User not found with email: {}", connectedUser.getName());
            return ResponseUtil.getFailureResponse("User not found.");
        }
        if(!Objects.equals(changePasswordRequest.getPassword(), changePasswordRequest.getConfirmPassword())){
            LOG.info("Password  and confirm password do not match.");
            return ResponseUtil.getFailureResponse("Password and confirm password do not match.");
        }
        if(Objects.equals(changePasswordRequest.getOldPassword(),user.getPassword())){
            LOG.info("Old password do not match: {}",connectedUser.getName());
            return ResponseUtil.getFailureResponse("Old password do not match-.");
        }
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(),user.getPassword())){
            LOG.info("Old password do not match: {}",connectedUser.getName());
            return ResponseUtil.getFailureResponse("Old password do not match.");
        }
        User changePassword = userMapper.changePassword(user, changePasswordRequest);
        userRepository.save(changePassword);
        LOG.info("User can change password successfully:{}",connectedUser.getName());

        List<UserToken> activeToken = userTokenRepository.findAllByUserAndLoggedOutFalse(user);
        LOG.info("Invalidating {} active token(s) for user with email: {}", activeToken.size(), connectedUser.getName());
        for(UserToken token : activeToken){
            UserTokenUtil.invalidateToken(
                    token.getUser(),
                    userTokenRepository
            );
        }
        return ResponseUtil.getSuccessfulApiResponse("Password changed successfully");
    }

    @Override
    public ApiResponse<?> viewProfile(Principal loggedInUser, HttpServletRequest request) {
        User user = userRepository.findByEmail(loggedInUser.getName());
        if (user == null){
            LOG.info("Failed to view profile. User not found with email: {}", loggedInUser.getName());
            return ResponseUtil.getFailureResponse("User not found.");
        }
        UserResponse userResponse = userMapper.entityToResponse(user);
        return ResponseUtil.getSuccessfulApiResponse(userResponse, "Successfully retrieved user profile.");
    }

}
