package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 脚本节点状态枚举（用于ASR匹配）
 *
 * @author DongBoot
 */
public enum ScriptNodeStatusEnum {

    /**
     * 未讲
     */
    PENDING(0, "未讲"),

    /**
     * 当前
     */
    CURRENT(1, "当前"),

    /**
     * 已讲
     */
    SPOKEN(2, "已讲"),

    ;

    private final int code;

    private final String desc;

    ScriptNodeStatusEnum(int code, String desc) {
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
    public static ScriptNodeStatusEnum fromCode(int code) {
        for (ScriptNodeStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

}