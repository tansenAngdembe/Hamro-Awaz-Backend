package com.tansen.app.mapper;


import com.tansen.app.dto.model.EmailOtpSendDto;
import com.tansen.app.util.EmailContentUtil;
import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.entity.User;
import com.tansen.entity.UserEmailLog;
import com.tansen.entity.UserRegistrationEmailLog;
import com.tansen.repository.UserEmailLogRepository;
import com.tansen.repository.UserRegistrationEmailLogRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EmailMapper {
    @Autowired
    private EmailContentUtil emailContentUtil;
    @Autowired
    private UserEmailLogRepository userEmailLogRepository;
    @Autowired
    private UserRegistrationEmailLogRepository userRegistrationEmailLogRepository;


    public UserEmailLog mapOtpSendRequest(User user, ForgotPasswordOtp otp) {
        String uuid = UuidUtil.generateUuid();

        Date now = new Date();
        long fiveMinutesInMillis = 5 * 60 * 1000;
        Date expirationTime = new Date(now.getTime() + fiveMinutesInMillis);

        EmailOtpSendDto emailOtpSendDto = new EmailOtpSendDto();
        emailOtpSendDto.setUserFullName(user.getFullName());
        emailOtpSendDto.setOtp(otp.getOtp());
        emailOtpSendDto.setExpirationTime(expirationTime);
        emailOtpSendDto.setTemplateName(EmailTemplateNameConstant.USER_FORGOT_PASSWORD);

        String emailContent = emailContentUtil.prepareOtpEmailContent(emailOtpSendDto);

        UserEmailLog userEmailLog = new UserEmailLog();
        userEmailLog.setEmail(user.getEmail());
        userEmailLog.setUser(user);
        userEmailLog.setUuid(uuid);
        userEmailLog.setMessage(emailContent);
        userEmailLog.setIsExpired(false);

        userEmailLogRepository.save(userEmailLog);
        return userEmailLog;

    }


    public UserEmailLog mapToRegisterUser(User user, UserRegistrationEmailLog otp) {
        String uuid = UuidUtil.generateUuid();
        Date now = new Date();
        long fiveMinutesInMillis = 5 * 60 * 1000;
        Date expirationTime = new Date(now.getTime() + fiveMinutesInMillis);

        EmailOtpSendDto emailOtpSendDto = new EmailOtpSendDto();
        emailOtpSendDto.setUserFullName(user.getFullName());
        emailOtpSendDto.setOtp(Integer.parseInt(otp.getOtp()));
        emailOtpSendDto.setExpirationTime(expirationTime);
        emailOtpSendDto.setTemplateName(EmailTemplateNameConstant.USER_ACCOUNT_VERIFICATION);

        String emailContent = emailContentUtil.prepareOtpEmailContent(emailOtpSendDto);

        UserEmailLog userEmailLog = new UserEmailLog();
        userEmailLog.setEmail(user.getEmail());
        userEmailLog.setUser(user);
        userEmailLog.setUuid(uuid);
        userEmailLog.setMessage(emailContent);
        userEmailLog.setIsSent(true);
        userEmailLogRepository.save(userEmailLog);
        return userEmailLog;

    }
}
