package com.tansen.government.core.util;

public class OtpUtil {
        public static int generateOtp() {
            return (int) (Math.random() * 900000) + 100000;
        }
    }

