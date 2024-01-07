package com.littlepants.attack.attackplus.exception;

import java.io.Serial;

/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2023/4/12
 * @description
 */
public class BaseUncheckedException extends RuntimeException implements BaseException{
    @Serial
    private static final long serialVersionUID = -778887391066124051L;

    /**
     * 异常信息
     */
    protected String message;

    /**
     * 具体异常码
     */
    protected int code;

    public BaseUncheckedException(int code,String message){
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(int code,String format,Object... args){
        super(String.format(format,args));
        this.code = code;
        this.message = String.format(format,args);
    }

    @Override
    public String getMessage(){
        return message;
    }
    @Override
    public int getCode() {
        return code;
    }
}
