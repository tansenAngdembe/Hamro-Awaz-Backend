package com.tansen.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    String uploadFile(MultipartFile file, String baseDirectory, String finalDirectory, boolean isImage) throws IOException;

}
