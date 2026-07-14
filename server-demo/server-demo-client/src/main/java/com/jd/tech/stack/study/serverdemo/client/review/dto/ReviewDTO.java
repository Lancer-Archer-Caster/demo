package com.jd.tech.stack.study.serverdemo.client.review.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Description: 复盘DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ReviewDTO {

    /** 场次ID */
    private Long sessionId;

    /** 场次名称 */
    private String sessionName;

    /** 日期 */
    private String date;

    /** 指标数据 */
    private Map<String, Object> metrics;

    /** 告警摘要 */
    private String alertSummary;

    /** 弹幕摘要 */
    private String danmakuSummary;

    /** 知识条目 */
    private List<String> knowledgeItems;

    /** 时间线 */
    private List<Map<String, Object>> timeline;
}