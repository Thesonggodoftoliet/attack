package com.littlepants.attack.attackweb.handler;

import com.littlepants.attack.attackweb.response.CustomResponseEntity;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ExceptionAdvice{
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public String FileSizeLimit(MultipartException e){
        e.printStackTrace();
        return new CustomResponseEntity(500,"文件大小不能超过1M",null).toString();
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public String Limit(MaxUploadSizeExceededException e){
        e.printStackTrace();
        return new CustomResponseEntity(500,"文件大小不能超过1M",null).toString();
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseBody
    public String Limit(FileSizeLimitExceededException e){
        e.printStackTrace();
        return new CustomResponseEntity(500,"文件大小不能超过1M",null).toString();
    }
}
