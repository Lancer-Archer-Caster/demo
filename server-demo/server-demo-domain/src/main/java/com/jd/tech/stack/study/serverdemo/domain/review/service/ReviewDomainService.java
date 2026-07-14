package com.jd.tech.stack.study.serverdemo.domain.review.service;

import com.jd.tech.stack.study.serverdemo.domain.review.bo.ReviewBO;
import com.jd.tech.stack.study.serverdemo.domain.review.converter.ReviewDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.review.dataobject.ReviewDO;
import com.jd.tech.stack.study.serverdemo.infra.review.gateway.ReviewGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 复盘领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ReviewDomainService {

    private static final Logger log = LoggerFactory.getLogger(ReviewDomainService.class);

    @Autowired
    private ReviewGateway reviewGateway;

    public ReviewBO getSessionReview(Long sessionId) {
        log.info("getSessionReview sessionId={}", sessionId);
        ReviewDO reviewDO = reviewGateway.findBySessionId(sessionId);
        ReviewBO review = ReviewDOConverter.convert(reviewDO);
        if (review == null) {
            review = new ReviewBO();
            review.setSessionId(sessionId);
            review.setSessionName("5G智能手机本地联通演示");
            review.setDate("2026-07-14");
        }
        if (review.getMetrics() == null) {
            Map<String, Object> metrics = new LinkedHashMap<>();
            metrics.put("totalGMV", 1200500);
            metrics.put("totalOrders", 3456);
            metrics.put("avgOrderValue", 34730);
            metrics.put("refundRate", 2.5);
            metrics.put("peakViewers", 12853);
            metrics.put("avgViewers", 8420);
            metrics.put("conversionRate", 4.2);
            review.setMetrics(metrics);
        }
        if (review.getAlertSummary() == null) {
            review.setAlertSummary("本地告警 1 条，已接入 H2");
        }
        if (review.getDanmakuSummary() == null) {
            review.setDanmakuSummary("评论通过统一接入接口进入本地分析链路");
        }
        if (review.getKnowledgeItems() == null) {
            review.setKnowledgeItems(Arrays.asList("快充参数说明", "续航口播建议", "NFC功能说明"));
        }
        if (review.getTimeline() == null) {
            List<Map<String, Object>> timeline = new ArrayList<>();
            timeline.add(event("14:00", "直播来源接入", "start"));
            timeline.add(event("14:05", "收到第一条评论", "comment"));
            timeline.add(event("14:10", "完成本地规则分析", "analysis"));
            review.setTimeline(timeline);
        }
        return review;
    }

    public Boolean backflowKnowledge(Long sessionId) {
        log.info("backflowKnowledge sessionId={}", sessionId);
        return reviewGateway.backflowKnowledge(sessionId);
    }

    public String exportReport(Long sessionId) {
        log.info("exportReport sessionId={}", sessionId);
        String report = reviewGateway.exportReport(sessionId);
        return report == null || report.trim().isEmpty()
                ? "JoyCue 本地复盘报告：场次 " + sessionId + "，评论接入、规则分析与话术生成链路运行正常。"
                : report;
    }

    private Map<String, Object> event(String time, String description, String type) {
        Map<String, Object> event = new LinkedHashMap<>();
        event.put("time", time);
        event.put("event", description);
        event.put("type", type);
        return event;
    }
}
