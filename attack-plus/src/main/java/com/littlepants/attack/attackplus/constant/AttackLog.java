package com.littlepants.attack.attackplus.constant;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/7/11
 */

public enum AttackLog implements BaseAttackLog{
    SUCCESS(1,"执行成功"),
    BEGIN(0, "开始执行"),
    TERMINATE(-1, "终止执行"),
    FAIL(2,"执行失败");

    private int code;
    private String msg;

    AttackLog(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
