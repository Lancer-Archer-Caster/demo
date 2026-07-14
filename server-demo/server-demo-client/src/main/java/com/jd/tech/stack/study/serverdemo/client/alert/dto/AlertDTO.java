package com.jd.tech.stack.study.serverdemo.client.alert.dto;

import lombok.Data;

/**
 * Description: 告警DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class AlertDTO {

    /** 告警ID */
    private Long id;

    /** 场次ID */
    private Long sessionId;

    /** 告警类型 */
    private String alertType;

    /** 商品ID */
    private String skuId;

    /** 告警内容 */
    private String content;

    /** 告警级别 */
    private String level;

    /** 创建时间 */
    private String createdAt;
}