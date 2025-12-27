package com.tansen.repository;

import com.tansen.entity.ForgotPasswordOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgotPasswordOtpRepository extends JpaRepository<ForgotPasswordOtp, Long> {
    ForgotPasswordOtp findByUserUniqueIdAndOtpAndIsValidTrue(String uniqueId, int otp);

    List<ForgotPasswordOtp> findByUserUniqueIdAndIsValidTrue(String uniqueId);
}
