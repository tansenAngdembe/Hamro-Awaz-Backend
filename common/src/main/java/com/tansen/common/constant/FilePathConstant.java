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
    public static final String COMPLAINT = "/complaint/";
    public static final String MUNICIPALITY = "/municipality/";
    public static final String MUNICIPALITY_DOCUMENT = "/municipality_document/";
    public static final String AUTHORITY_USER = "/authority_user/";
    public static final String AUTHORITY_SERVICE = "/authority_service/";
    public static final String AUTHORITY_LINE = "/authority_line/";
    public static final String ADVERTISEMENT = "/advertisement/";
    public static final String CITIZENSHIPCARDFRONT = "/citizenshipCardFront/";
    public static final String CITIZENSHIPCARDBACK = "/citizenshipCardBack/";
}
