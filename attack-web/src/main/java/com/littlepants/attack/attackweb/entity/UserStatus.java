package com.littlepants.attack.attackweb.entity;

public enum UserStatus {
    ACTIVE("active"),
    BLOCKED("blocked");
    private String status;

    UserStatus(String status) {
        this.status = status;
    }
}
