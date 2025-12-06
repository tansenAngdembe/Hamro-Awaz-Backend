package com.tansen.common.utility;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageCompressionUtil {
    public static void compressImage(InputStream inputStream, String outputPath,float quality,String format) throws IOException {
        Thumbnails.of(inputStream)
                .scale(1)
                .outputQuality(quality)
                .outputFormat(format)
                .toFile(new File(outputPath));
    }
}
