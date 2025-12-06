package com.tansen.common.constant;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FilePathConstant {
    public static String BASE_PATH;
    @Value("${file.base-path}")
    private String basePathFromProperties;

    @PostConstruct
    public void init() {
        BASE_PATH = basePathFromProperties;
    }

    public static final String ADMIN = "/admin/";
    public static final String USER = "/user/";
    public static final String VENDOR = "/vendor/";
    public static final String VENDOR_DOCUMENT = "/vendor_document/";
    public static final String VENDOR_USER = "/vendor_user/";
    public static final String VENDOR_SERVICE = "/vendor_service/";
    public static final String VENDOR_LINE = "/vendor_line/";
    public static final String ADVERTISEMENT = "/advertisement/";
}
