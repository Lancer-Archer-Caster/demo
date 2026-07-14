package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 合规词分类枚举
 *
 * @author DongBoot
 */
public enum ComplianceCategoryEnum {

    /**
     * 极限词
     */
    EXTREME(1, "极限词"),

    /**
     * 拉踩词
     */
    DISPARAGE(2, "拉踩词"),

    /**
     * 敏感词
     */
    SENSITIVE(3, "敏感词"),

    ;

    private final int code;

    private final String desc;

    ComplianceCategoryEnum(int code, String desc) {
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
     * @param code 分类编码
     * @return 对应的枚举值，未匹配则返回null
     */
    public static ComplianceCategoryEnum fromCode(int code) {
        for (ComplianceCategoryEnum category : values()) {
            if (category.getCode() == code) {
                return category;
            }
        }
        return null;
    }

}