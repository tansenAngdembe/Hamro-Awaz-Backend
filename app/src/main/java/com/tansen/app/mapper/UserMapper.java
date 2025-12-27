package com.tansen.app.mapper;



import com.tansen.app.dto.request.ChangePasswordRequest;
import com.tansen.app.dto.request.CreateUserRequest;
import com.tansen.app.dto.request.SetForgotPasswordRequest;
import com.tansen.app.dto.request.UserProfileUpdateRequest;
import com.tansen.app.dto.response.UserProfileResponse;
import com.tansen.common.constant.EmailSubjectConstant;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.dto.response.UserResponse;
import com.tansen.common.service.MailService;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.entity.User;
import com.tansen.entity.UserEmailLog;
import com.tansen.entity.UserRegistrationEmailLog;
import com.tansen.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ForgotPasswordOtpRepository forgotPasswordOtpRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private EmailMapper emailMapper;
    @Autowired
    private UserRegistrationEmailLogRepository userRegistrationEmailLogRepository;
    @Autowired
    private UserRegistrationEmailLogMapper userRegistrationEmailLogMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private RoleRepository roleRepository;

    public abstract UserProfileResponse userProfilePhotoResponse(User user);

    public User mapToEntity(CreateUserRequest request, PasswordEncoder passwordEncoder, HttpServletRequest httpServletRequest) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUniqueId(UuidUtil.generateUuid());
        user.setStatus(statusRepository.findByName(StatusConstant.PENDING.getName()));
        user.setRole( roleRepository.findByName("USER"));
        user.setWrongPasswordAttemptCount(0);
        user.setRegisteredDate(LocalDateTime.now());
        User savedUser = userRepository.save(user);

        List<UserRegistrationEmailLog> validOtp = userRegistrationEmailLogRepository.findByUserAndIsOtpExpiredFalse(user);
        if (!validOtp.isEmpty()){
            validOtp.forEach(otp -> otp.setIsOtpExpired(true));
            userRegistrationEmailLogRepository.saveAll(validOtp);
        }
        UserRegistrationEmailLog userRegistrationEmailLog = userRegistrationEmailLogMapper.mapToEntity(user);
        UserRegistrationEmailLog savedOtp = userRegistrationEmailLogRepository.save(userRegistrationEmailLog);
        UserEmailLog userEmailLog = emailMapper.mapToRegisterUser(savedUser, savedOtp);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(user.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.USER_ACCOUNT_REGISTRATION);
        sendEmailRequest.setMessage(userEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);

        return  savedUser;
    }

    public void sendAccountResetEmail(User user) {
        List<UserRegistrationEmailLog> byUserAndIsOtpExpiredFalse = userRegistrationEmailLogRepository.findByUserAndIsOtpExpiredFalse(user);
        for (UserRegistrationEmailLog userRegistrationEmailLog : byUserAndIsOtpExpiredFalse) {
            userRegistrationEmailLog.setIsOtpExpired(true);
            userRegistrationEmailLogRepository.save(userRegistrationEmailLog);
        }
        UserRegistrationEmailLog userRegistrationEmailLog = userRegistrationEmailLogMapper.mapToEntity(user);
        UserRegistrationEmailLog savedOtp = userRegistrationEmailLogRepository.save(userRegistrationEmailLog);
        UserEmailLog userEmailLog = emailMapper.mapToRegisterUser(user, savedOtp);
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.setRecipient(user.getEmail());
        sendEmailRequest.setSubject(EmailSubjectConstant.USER_ACCOUNT_REGISTRATION);
        sendEmailRequest.setMessage(userEmailLog.getMessage());
        mailService.sendEmail(sendEmailRequest);

    }

    public User updateProfile(MultipartFile profilePicture, User editUser, UserProfileUpdateRequest userProfileUpdateRequest) throws IOException {
        editUser.setFullName(userProfileUpdateRequest.getFullName());
        editUser.setPhoneNumber(userProfileUpdateRequest.getMobileNumber());
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String uploadedLink = uploadFileService.uploadFile(profilePicture, FilePathConstant.BASE_PATH, FilePathConstant.USER, true);
            editUser.setProfilePictureLink(uploadedLink);
        }
        return editUser;
    }

    public User changePassword(User user, ChangePasswordRequest changePasswordRequest) {
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        user.setPasswordChangeDate(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public void forgotPasswordChange(User user, ForgotPasswordOtp validUsrOtp, SetForgotPasswordRequest forgotPasswordRequest){
        user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
        user.setPasswordChangeDate(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        if (validUsrOtp.getIsValid()) {
            validUsrOtp.setIsValid(false);
            forgotPasswordOtpRepository.save(validUsrOtp);
        }
    }

    public void verifyUserRegistrationOtp(User user, UserRegistrationEmailLog userRegistrationEmailLog) {
        user.setStatus(statusRepository.findByName(StatusConstant.ACTIVE.getName()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        if (!userRegistrationEmailLog.getIsOtpExpired()) {
            userRegistrationEmailLog.setIsOtpExpired(true);
            userRegistrationEmailLogRepository.save(userRegistrationEmailLog);
        }
    }

    public abstract UserResponse entityToResponse(User user);


}
