package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 知识来源枚举
 *
 * @author DongBoot
 */
public enum KnowledgeSourceEnum {

    /**
     * 人工录入
     */
    MANUAL(1, "人工录入"),

    /**
     * AI生成
     */
    AI_GENERATED(2, "AI生成"),

    /**
     * 知识回流
     */
    BACKFLOW(3, "知识回流"),

    ;

    private final int code;

    private final String desc;

    KnowledgeSourceEnum(int code, String desc) {
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
     * @param code 来源编码
     * @return 对应的枚举值，未匹配则返回null
     */
    public static KnowledgeSourceEnum fromCode(int code) {
        for (KnowledgeSourceEnum source : values()) {
            if (source.getCode() == code) {
                return source;
            }
        }
        return null;
    }

}