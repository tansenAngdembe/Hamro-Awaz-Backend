package com.tansen.government.emaillog.mapper;

import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.ExpirationTimeUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
import com.tansen.government.emaillog.dto.AuthorityUserEmailContent;
import com.tansen.government.emaillog.util.AuthorityUserEmailUtil;
import com.tansen.repository.AuthorityUserEmailLogRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AuthorityUserEmailLogMapper {

    @Autowired
    private AuthorityUserEmailUtil authorityUserEmailUtil;
    @Autowired
    private AuthorityUserEmailLogRepository authorityUserEmailLogRepository;


    public AuthorityUserEmailLog mapToVendor(AuthorityUser authorityUser){
        String uuid = UuidUtil.generateUuid();
        Date expirationTime = ExpirationTimeUtil.getExpirationTime(60*24);

        AuthorityUserEmailContent adminRegistrationEmailContent = new AuthorityUserEmailContent();
        adminRegistrationEmailContent.setName(authorityUser.getName());
        adminRegistrationEmailContent.setUuid(uuid);
        adminRegistrationEmailContent.setExpirationTime(expirationTime);
        adminRegistrationEmailContent.setTemplate(EmailTemplateNameConstant.GOVERNMENT_USER_ACCOUNT_VERIFICATION);
        String content = authorityUserEmailUtil.prepareVendorUserEmail(adminRegistrationEmailContent,"http://localhost:5173/setPassword/");

        AuthorityUserEmailLog vendorUserEmailLog = new AuthorityUserEmailLog();
        vendorUserEmailLog.setUniqueId(uuid);
        vendorUserEmailLog.setAuthorityUser(authorityUser);
        vendorUserEmailLog.setEmail(authorityUser.getEmail());
        vendorUserEmailLog.setMessage(content);
        vendorUserEmailLog.setIsSent(true);
        vendorUserEmailLog.setIsExpired(false);
        vendorUserEmailLog.setCreatedAt(LocalDateTime.now());
        authorityUserEmailLogRepository.save(vendorUserEmailLog);
        return vendorUserEmailLog;
    }


    public AuthorityUserEmailLog mapToSendPasswordResetLink(AuthorityUser authorityUser){
        String uuid = UuidUtil.generateUuid();
        Date expirationTime = ExpirationTimeUtil.getExpirationTime(60*24);

        AuthorityUserEmailContent vendorSendPasswordResetLinkEmail = new AuthorityUserEmailContent();
        vendorSendPasswordResetLinkEmail.setName(authorityUser.getName());
        vendorSendPasswordResetLinkEmail.setUuid(uuid);
        vendorSendPasswordResetLinkEmail.setExpirationTime(expirationTime);
        vendorSendPasswordResetLinkEmail.setTemplate(EmailTemplateNameConstant.SEND_PASSWORD_RESET_LINK_FOR_ADMIN);
        String content = authorityUserEmailUtil.prepareVendorUserEmail(vendorSendPasswordResetLinkEmail,"http://localhost:5173/resetPassword/");

        AuthorityUserEmailLog adminEmailLog = new AuthorityUserEmailLog();
        adminEmailLog.setUniqueId(uuid);
        adminEmailLog.setAuthorityUser(authorityUser);
        adminEmailLog.setEmail(authorityUser.getEmail());
        adminEmailLog.setMessage(content);
        adminEmailLog.setIsExpired(false);
        adminEmailLog.setIsSent(true);
        adminEmailLog.setCreatedAt(LocalDateTime.now());
        return adminEmailLog;
    }

}
