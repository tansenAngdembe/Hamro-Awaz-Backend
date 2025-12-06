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
        assert originalFileName != null;
        String fileExtension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
        String refactoredFileName = originalFileName.replace(" ", "-");
        if (isImage) {
            if (!fileExtension.equalsIgnoreCase(".jpg") &&
                    !fileExtension.equalsIgnoreCase(".jpeg") &&
                    !fileExtension.equalsIgnoreCase(".png")) {
                throw new InvalidFileFormatException("Unsupported file format. Only jpg, jpeg and png images are supported" + fileExtension.toLowerCase());

            }
        } else {
            if (!fileExtension.equalsIgnoreCase(".pdf")) {
                throw new InvalidFileFormatException("Only pdf document are supported." + fileExtension.toLowerCase());
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
