package com.tansen.app.mapper;

import com.tansen.app.dto.request.ForgotPasswordRequest;
import com.tansen.app.util.OtpUtil;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.Duration;
import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public class ForgotPasswordOptMapper {

    public ForgotPasswordOtp mapToEntity(ForgotPasswordRequest forgotPasswordRequest, User user) {
        Instant expiryTime = Instant.now().plus(Duration.ofMinutes(5));
        int otp = OtpUtil.generateOtp();

        ForgotPasswordOtp forgotPasswordOtp = new ForgotPasswordOtp();
        forgotPasswordOtp.setOtp(otp);
        forgotPasswordOtp.setEmail(forgotPasswordRequest.getEmail());
        forgotPasswordOtp.setUserUniqueId(user.getUniqueId());
        forgotPasswordOtp.setValidDate(expiryTime);
        forgotPasswordOtp.setIsValid(true);
        forgotPasswordOtp.setCreatedAt(Instant.now());
        return forgotPasswordOtp;
    }

}
