package com.littlepants.attack.attackweb.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class Payload<T>{
    private String id;
    private T userinfo;
    private Date expiration;
}
