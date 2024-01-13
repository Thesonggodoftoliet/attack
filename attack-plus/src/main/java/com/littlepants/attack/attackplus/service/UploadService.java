package com.littlepants.attack.attackplus.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2024/1/13
 */

public interface UploadService {
    String uploadImg(MultipartFile multipartFile, String dir);
}
