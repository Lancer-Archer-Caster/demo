package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 直播场次状态枚举
 *
 * @author DongBoot
 */
public enum SessionStatusEnum {

    /**
     * 准备中
     */
    PREPARING(0, "准备中"),

    /**
     * 直播中
     */
    LIVING(1, "直播中"),

    /**
     * 已结束
     */
    ENDED(2, "已结束"),

    ;

    private final int code;

    private final String desc;

    SessionStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 状态编码
     * @return 对应的枚举值，未匹配则返回null
     */
    public static SessionStatusEnum fromCode(int code) {
        for (SessionStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

}