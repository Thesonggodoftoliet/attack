package com.littlepants.attack.attackweb.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class CustomResponseEntity {
    @ApiModelProperty(value = "状态码",example = "200")
    private int code;
    @ApiModelProperty(value = "返回信息",example = "success")
    private String msg;
    @ApiModelProperty(value = "返回数据")
    private Object obj;
    public CustomResponseEntity(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.msg = responseCode.getMsg();
    }
    public CustomResponseEntity(ResponseCode responseCode, Object obj){
        this(responseCode);
        this.obj =obj;
    }

    public CustomResponseEntity(int code, String msg, Object obj){
        this.code =code;
        this.msg = msg;
        this.obj = obj;
    }

    /**
     *当错误信息不是一个数组时使用此方法转换为String
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"msg\":\"")
                .append(msg).append('\"');
        sb.append(",\"obj\":")
                .append(obj);
        sb.append('}');
        return sb.toString();
    }

    /**
     * 除上述情况外使用此方法
     * @return
     */
    public String msgToString(){
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":")
                .append(code);
        sb.append(",\"msg\":")
                .append(msg);
        sb.append(",\"obj\":")
                .append(obj);
        sb.append('}');
        return sb.toString();
    }
}
