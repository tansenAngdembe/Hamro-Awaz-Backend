package com.tansen.app.mapper;

import com.tansen.app.util.OtpUtil;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.User;
import com.tansen.entity.UserRegistrationEmailLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserRegistrationEmailLogMapper {

    public UserRegistrationEmailLog mapToEntity(User user) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        int otp = OtpUtil.generateOtp();
        String otpString = String.valueOf(otp);

        UserRegistrationEmailLog userRegistrationEmailLog = new UserRegistrationEmailLog();
        userRegistrationEmailLog.setUniqueId(UuidUtil.generateUuid());
        userRegistrationEmailLog.setOtp(otpString);
        userRegistrationEmailLog.setEmail(user.getEmail());
        userRegistrationEmailLog.setUser(user);
        userRegistrationEmailLog.setIsOtpExpired(false);
        userRegistrationEmailLog.setIsSent(true);
        userRegistrationEmailLog.setExpirationTime(expiryTime);
        userRegistrationEmailLog.setMessage("User registration OTP");
        userRegistrationEmailLog.setTimestamp(LocalDateTime.now());
        return userRegistrationEmailLog;
    }
}

