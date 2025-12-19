package com.tansen.admin.emaillog.mapper;


import com.tansen.admin.emaillog.dto.AuthorityUserEmailContent;
import com.tansen.admin.emaillog.util.AuthorityUserEmailUtil;
import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.ExpirationTimeUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
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
    private AuthorityUserEmailUtil vendorUserEmailUtil;
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
        String content = vendorUserEmailUtil.prepareAdminEmail(adminRegistrationEmailContent,"http://localhost:5174/setPassword/");

        AuthorityUserEmailLog authorityUserEmailLog = new AuthorityUserEmailLog();
        authorityUserEmailLog.setUniqueId(uuid);
        authorityUserEmailLog.setAuthorityUser(authorityUser);
        authorityUserEmailLog.setEmail(authorityUser.getEmail());
        authorityUserEmailLog.setMessage(content);
        authorityUserEmailLog.setIsSent(true);
        authorityUserEmailLog.setIsExpired(false);
        authorityUserEmailLog.setCreatedAt(LocalDateTime.now());
        authorityUserEmailLogRepository.save(authorityUserEmailLog);
        return authorityUserEmailLog;
    }

}
