package com.jd.tech.stack.study.serverdemo.infra.review.dataobject;

/**
 * Description: 复盘DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ReviewDO {

    private Long id;

    private Long sessionId;

    private String title;

    private String summary;

    private String metricsData;

    private String highlights;

    private String improvements;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getMetricsData() { return metricsData; }
    public void setMetricsData(String metricsData) { this.metricsData = metricsData; }

    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }

    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}