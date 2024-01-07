package com.littlepants.attack.attackplus.utils.jwt;

import lombok.Data;
import java.util.Date;

@Data
public class Payload<T>{
    private String id;
    private T userinfo;
    private Date expiration;
}
