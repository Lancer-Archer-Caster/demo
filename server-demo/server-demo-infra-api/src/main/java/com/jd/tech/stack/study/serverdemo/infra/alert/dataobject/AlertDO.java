package com.jd.tech.stack.study.serverdemo.infra.alert.dataobject;

/**
 * Description: 告警DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class AlertDO {

    private Long id;

    private Long sessionId;

    private String alertType;

    private String level;

    private String skuId;

    private String message;

    private String handled;

    private String handler;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getSkuId() { return skuId; }
    public void setSkuId(String skuId) { this.skuId = skuId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getHandled() { return handled; }
    public void setHandled(String handled) { this.handled = handled; }

    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}