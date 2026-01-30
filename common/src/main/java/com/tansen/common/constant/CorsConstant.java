package com.tansen.common.constant;

import java.util.List;

public class CorsConstant {
    public static final List<String> ADMIN_ALLOWED_ORIGINS = List.of(
            "http://localhost:5174/",
             "http://192.168.1.77:5174",
            "http://192.168.0.111:5174",
            "http://10.91.91.30:5174"
    );
    public static final List<String> GOVERNMENT_ALLOWED_ORIGINS = List.of(
            "http://localhost:5173/"
    );

    public static final List<String> APP_ALLOWED_ORIGINS = List.of(
            "http://localhost:9080",
            "http://localhost:8088",
            "http://localhost:9002"
    );
}
