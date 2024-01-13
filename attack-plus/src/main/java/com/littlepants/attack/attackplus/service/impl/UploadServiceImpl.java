package com.littlepants.attack.attackplus.service.impl;

import cn.hutool.core.lang.UUID;
import com.littlepants.attack.attackplus.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/13
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    @Value("${file.uploadFolder}")
    private String uploadFolder;
    @Value("${file.staticPath}")
    private String staticPath;
    @Override
    public String uploadImg(MultipartFile multipartFile, String dir) {
        try {
            String realFileName = multipartFile.getOriginalFilename();
            assert realFileName != null;
            String suffix = realFileName.substring(realFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID() +suffix;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = dateFormat.format(new Date());

            String serverPath = uploadFolder;
            File targetPath = new File(serverPath+dir,datePath);
            boolean tag = true;
            if (!targetPath.exists())
                tag = targetPath.mkdirs();
            if (!tag)
                return "error";
            File targetFileName = new File(targetPath,newFileName);
            multipartFile.transferTo(targetFileName);
            String fileName = dir+"/"+datePath+"/"+newFileName;
            return staticPath+fileName;
        }catch (IOException e){
            log.error(e.getMessage());
            return "error";
        }
    }
}
