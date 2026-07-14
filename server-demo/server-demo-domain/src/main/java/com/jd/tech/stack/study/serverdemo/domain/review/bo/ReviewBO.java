package com.jd.tech.stack.study.serverdemo.domain.review.bo;

import java.util.List;
import java.util.Map;

/**
 * Description: 复盘BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ReviewBO {

    private Long sessionId;

    private String sessionName;

    private String date;

    private Map<String, Object> metrics;

    private String alertSummary;

    private String danmakuSummary;

    private List<String> knowledgeItems;

    private List<Map<String, Object>> timeline;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public void setMetrics(Map<String, Object> metrics) {
        this.metrics = metrics;
    }

    public String getAlertSummary() {
        return alertSummary;
    }

    public void setAlertSummary(String alertSummary) {
        this.alertSummary = alertSummary;
    }

    public String getDanmakuSummary() {
        return danmakuSummary;
    }

    public void setDanmakuSummary(String danmakuSummary) {
        this.danmakuSummary = danmakuSummary;
    }

    public List<String> getKnowledgeItems() {
        return knowledgeItems;
    }

    public void setKnowledgeItems(List<String> knowledgeItems) {
        this.knowledgeItems = knowledgeItems;
    }

    public List<Map<String, Object>> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<Map<String, Object>> timeline) {
        this.timeline = timeline;
    }
}