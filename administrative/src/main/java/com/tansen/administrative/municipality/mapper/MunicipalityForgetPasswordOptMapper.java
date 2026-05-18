package com.tansen.administrative.municipality.mapper;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.ForgotPasswordOtp;
import com.tansen.administrative.core.util.OtpUtil;
import com.tansen.administrative.municipality.dto.ForgotPasswordRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.Duration;
import java.time.Instant;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public class MunicipalityForgetPasswordOptMapper {
    public ForgotPasswordOtp mapToEntity(ForgotPasswordRequest forgotPasswordRequest, AuthorityUser vendorUsers) {
        Instant expiryTime = Instant.now().plus(Duration.ofMinutes(5));
        int otp = OtpUtil.generateOtp();

        ForgotPasswordOtp forgotPasswordOtp = new ForgotPasswordOtp();
        forgotPasswordOtp.setOtp(otp);
        forgotPasswordOtp.setEmail(forgotPasswordRequest.getEmail());
        forgotPasswordOtp.setUserUniqueId(vendorUsers.getUniqueId());
        forgotPasswordOtp.setValidDate(expiryTime);
        forgotPasswordOtp.setIsValid(true);
        forgotPasswordOtp.setCreatedAt(Instant.now());
        return forgotPasswordOtp;
    }
}
