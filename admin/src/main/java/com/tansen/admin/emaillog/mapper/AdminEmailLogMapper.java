package com.tansen.admin.emaillog.mapper;


import com.tansen.admin.emaillog.dto.AdminEmailContent;
import com.tansen.admin.emaillog.util.AdminEmailUtil;
import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.ExpirationTimeUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.Admin;
import com.tansen.entity.AdminEmailLog;
import com.tansen.repository.AdminEmailLogRepository;
import com.tansen.repository.AdminRepository;
import com.tansen.repository.EmailTemplateRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AdminEmailLogMapper {
    @Autowired
    protected AdminEmailLogRepository adminEmailLogRepository;
    @Autowired
    protected EmailTemplateRepository emailTemplateRepository;
    @Autowired
    protected AdminRepository adminRepository;
    @Autowired
    protected AdminEmailUtil adminEmailUtil;

    public AdminEmailLog mapToAdmin(Admin admin){
        String uuid = UuidUtil.generateUuid();
        Date expirationTime = ExpirationTimeUtil.getExpirationTime(60*24);

        AdminEmailContent adminRegistrationEmailContent = new AdminEmailContent();
        adminRegistrationEmailContent.setName(admin.getName());
        adminRegistrationEmailContent.setUuid(uuid);
        adminRegistrationEmailContent.setExpirationTime(expirationTime);
        adminRegistrationEmailContent.setTemplate(EmailTemplateNameConstant.ADMIN_ACCOUNT_VERIFICATION);
        String content = adminEmailUtil.prepareAdminEmail(adminRegistrationEmailContent,"http://192.168.1.77:5174/setPassword/");

        AdminEmailLog adminEmailLog = new AdminEmailLog();
        adminEmailLog.setUuid(uuid);
        adminEmailLog.setAdmin(admin);
        adminEmailLog.setEmail(admin.getEmail());
        adminEmailLog.setMessage(content);
        adminEmailLog.setIsSent(true);
        adminEmailLog.setIsExpired(false);
        adminEmailLog.setCreatedAt(LocalDateTime.now());
        adminEmailLogRepository.save(adminEmailLog);
        return adminEmailLog;
    }

    public AdminEmailLog mapToSendPasswordResetLink(Admin admin){
        String uuid = UuidUtil.generateUuid();
        Date expirationTime = ExpirationTimeUtil.getExpirationTime(60*24);

        AdminEmailContent adminSendPasswordResetLinkEmail = new AdminEmailContent();
        adminSendPasswordResetLinkEmail.setName(admin.getName());
        adminSendPasswordResetLinkEmail.setUuid(uuid);
        adminSendPasswordResetLinkEmail.setExpirationTime(expirationTime);
        adminSendPasswordResetLinkEmail.setTemplate(EmailTemplateNameConstant.SEND_PASSWORD_RESET_LINK_FOR_ADMIN);
        String content = adminEmailUtil.prepareAdminEmail(adminSendPasswordResetLinkEmail,"http://localhost:5173/resetPassword/");

        AdminEmailLog adminEmailLog = new AdminEmailLog();
        adminEmailLog.setUuid(uuid);
        adminEmailLog.setAdmin(admin);
        adminEmailLog.setEmail(admin.getEmail());
        adminEmailLog.setMessage(content);
        adminEmailLog.setIsExpired(false);
        adminEmailLog.setIsSent(true);
        adminEmailLog.setCreatedAt(LocalDateTime.now());
        return adminEmailLog;
    }
}
