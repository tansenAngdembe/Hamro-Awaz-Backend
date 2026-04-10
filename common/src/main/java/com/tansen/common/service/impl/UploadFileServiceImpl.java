package com.tansen.common.service.impl;

import com.tansen.common.exception.InvalidFileFormatException;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.ImageCompressionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    public String uploadFile(MultipartFile file, String baseDirectory, String finalDirectory, boolean isImage) throws IOException {
        if(file == null){
            return  null;
        }
        String currentDateAndTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new InvalidFileFormatException("File name is missing.");
        }

// Safer extension extraction
        int dotIndex = originalFileName.lastIndexOf(".");
        String fileExtension = (dotIndex != -1) ? originalFileName.substring(dotIndex).toLowerCase() : "";

        System.out.println("DEBUG - filename: " + originalFileName + ", ext: " + fileExtension);

        String refactoredFileName = originalFileName.replace(" ", "-");

        if (isImage) {
            if (!fileExtension.equals(".jpg") &&
                    !fileExtension.equals(".jpeg") &&
                    !fileExtension.equals(".png")) {
                throw new InvalidFileFormatException(
                        "Unsupported file format. Only jpg, jpeg and png are supported. Got: " + fileExtension
                );
            }
        } else {
            if (!fileExtension.equals(".pdf")) {
                throw new InvalidFileFormatException(
                        "Only pdf documents are supported. Got: " + fileExtension
                );
            }
        }
        String finalFileName = currentDateAndTime + "-"+ refactoredFileName;
        File directory = new File(baseDirectory + finalDirectory);
        if(!directory.exists()){
            directory.mkdirs();
        }
        String filePath = baseDirectory + finalDirectory + finalFileName;
        if(isImage){
            String format = fileExtension.startsWith(".") ? fileExtension.substring(1) : fileExtension;
            ImageCompressionUtil.compressImage(file.getInputStream(),filePath,1f,format);
        }else {
            Files.copy(file.getInputStream(), Paths.get(filePath));
        }
        return finalDirectory + finalFileName;
    }
}
