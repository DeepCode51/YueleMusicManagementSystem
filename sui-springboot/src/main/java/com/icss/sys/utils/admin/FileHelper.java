package com.icss.sys.utils.admin;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileHelper {
    public static File saveMultiPartFile(MultipartFile myFile, String path, String fileName) throws IOException {
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        myFile.transferTo(targetFile);
        return targetFile;
    }

    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
