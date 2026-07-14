package com.jd.tech.stack.study.serverdemo.enums;

/**
 * Description: 告警类型枚举
 *
 * @author DongBoot
 */
public enum AlertTypeEnum {

    /**
     * 库存告警
     */
    INVENTORY("inventory", "库存告警"),

    /**
     * 流量峰值
     */
    TRAFFIC_PEAK("traffic_peak", "流量峰值"),

    /**
     * 价格变动
     */
    PRICE_CHANGE("price_change", "价格变动"),

    ;

    private final String code;

    private final String desc;

    AlertTypeEnum(String code, String desc) {
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
     * @param code 告警类型编码
     * @return 对应的枚举值，未匹配则返回null
     */
    public static AlertTypeEnum fromCode(String code) {
        for (AlertTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}