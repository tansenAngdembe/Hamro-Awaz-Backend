package com.tansen.government.municipality.mapper;

import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.ExpirationTimeUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.government.core.util.EmailContentUtil;
import com.tansen.government.emaillog.dto.AuthorityUserEmailContent;
import com.tansen.government.municipality.dto.EmailOtpSendDto;
import com.tansen.repository.AuthorityEmailLogRepository;
import com.tansen.repository.AuthorityUserEmailLogRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MunicipalityEmailMapper {
    @Autowired
    private EmailContentUtil emailContentUtil;
    @Autowired
    private AuthorityUserEmailLogRepository authorityEmailLogRepository;




    public AuthorityUserEmailLog mapOtpSendRequest(AuthorityUser vendorUsers, ForgotPasswordOtp otp) {
        String uuid = UuidUtil.generateUuid();

        Date now = new Date();
        long fiveMinutesInMillis = 5 * 60 * 1000;
        Date expirationTime = new Date(now.getTime() + fiveMinutesInMillis);

        EmailOtpSendDto emailOtpSendDto = new EmailOtpSendDto();
        emailOtpSendDto.setUserFullName(vendorUsers.getName());
        emailOtpSendDto.setOtp(otp.getOtp());
        emailOtpSendDto.setExpirationTime(expirationTime);
        emailOtpSendDto.setTemplateName(EmailTemplateNameConstant.VENDOR_FORGOT_PASSWORD);

        String emailContent = emailContentUtil.prepareOtpEmailContent(emailOtpSendDto);

        AuthorityUserEmailLog userEmailLog = new AuthorityUserEmailLog();
        userEmailLog.setEmail(vendorUsers.getEmail());
        userEmailLog.setAuthorityUser(vendorUsers);
        userEmailLog.setUniqueId(uuid);
        userEmailLog.setMessage(emailContent);
        userEmailLog.setIsExpired(false);

        authorityEmailLogRepository.save(userEmailLog);
        return userEmailLog;
    }
}