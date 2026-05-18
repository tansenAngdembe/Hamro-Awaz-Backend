package com.tansen.administrative.emaillog.mapper;

import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.ExpirationTimeUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.User;
import com.tansen.entity.UserEmailLog;
import com.tansen.administrative.emaillog.dto.UserEmailContent;
import com.tansen.administrative.emaillog.util.UserEmailUtil;
import com.tansen.repository.UserEmailLogRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserRegistrationEmailLogMapper {
    @Autowired
    private UserEmailUtil userEmailUtil;
    @Autowired
    private UserEmailLogRepository userEmailLogRepository;

    public UserEmailLog mapToEntity(User user) {
        String uuid = UuidUtil.generateUuid();
        Date expirationTime = ExpirationTimeUtil.getExpirationTime(60*24);

        UserEmailContent userEmailContent = new UserEmailContent();
        userEmailContent.setName(user.getFullName());
        userEmailContent.setUuid(uuid);
        userEmailContent.setExpirationTime(expirationTime);
        userEmailContent.setTemplate(EmailTemplateNameConstant.USER_REGISTRATION_VERIFICATION);
        String content = userEmailUtil.prepareUserEmail(userEmailContent,"http://localhost:5174/setPassword/");

        UserEmailLog userEmailLog = new UserEmailLog();
        userEmailLog.setUuid(uuid);
        userEmailLog.setUser(user);
        userEmailLog.setEmail(user.getEmail());
        userEmailLog.setIsSent(true);
        userEmailLog.setMessage(content);
        userEmailLog.setIsExpired(false);
        userEmailLog.setCreatedAt(LocalDateTime.now());
        return userEmailLogRepository.save(userEmailLog);
    }
}

