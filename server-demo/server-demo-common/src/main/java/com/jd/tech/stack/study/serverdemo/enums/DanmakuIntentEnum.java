package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 弹幕意图分类枚举
 *
 * @author DongBoot
 */
public enum DanmakuIntentEnum {

    /**
     * 水贴
     */
    WATER("water", "水贴"),

    /**
     * 咨询
     */
    QA("qa", "咨询"),

    /**
     * 投诉
     */
    COMPLAINT("complaint", "投诉"),

    /**
     * 其他
     */
    OTHER("other", "其他"),

    ;

    private final String code;

    private final String desc;

    DanmakuIntentEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举
     *
     * @param code 意图编码
     * @return 对应的枚举值，未匹配则返回null
     */
    public static DanmakuIntentEnum fromCode(String code) {
        for (DanmakuIntentEnum intent : values()) {
            if (intent.getCode().equals(code)) {
                return intent;
            }
        }
        return null;
    }

}