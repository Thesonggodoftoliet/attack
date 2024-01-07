package com.littlepants.attack.attackweb.service;

import com.littlepants.attack.attackweb.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UploadService {
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${file.staticPath}")
    private String staticPath;

    public String uploadImg(MultipartFile multipartFile,String dir){
        try {
            String realFileName = multipartFile.getOriginalFilename();
            String suffix = realFileName.substring(realFileName.lastIndexOf("."));
            String newFileName = UUIDGenerator.generateUUID()+suffix;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = dateFormat.format(new Date());

            String serverPath = uploadFolder;
            File targetPath = new File(serverPath+dir,datePath);
            if (!targetPath.exists())
                targetPath.mkdirs();
            File targetFileName = new File(targetPath,newFileName);
            multipartFile.transferTo(targetFileName);
            String fileName = dir+"/"+datePath+"/"+newFileName;
            return staticPath+fileName;
        }catch (IOException e){
            e.printStackTrace();
            return "error";
        }
    }
}
