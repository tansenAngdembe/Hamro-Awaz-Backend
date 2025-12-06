package com.tansen.common.utility;

import java.util.UUID;

public class UuidUtil {
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
