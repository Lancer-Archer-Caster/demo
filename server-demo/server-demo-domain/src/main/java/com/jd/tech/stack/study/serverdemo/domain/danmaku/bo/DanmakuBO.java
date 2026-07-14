package com.jd.tech.stack.study.serverdemo.domain.danmaku.bo;

/**
 * Description: 弹幕BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class DanmakuBO {

    private Long id;

    private Long sessionId;

    private String originalText;

    private String senderNick;

    private String intent;

    private String aiAnswer;

    private Boolean aiGenerated;

    private Boolean humanConfirmed;

    private Boolean replied;

    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getSenderNick() { return senderNick; }
    public void setSenderNick(String senderNick) { this.senderNick = senderNick; }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getAiAnswer() {
        return aiAnswer;
    }

    public void setAiAnswer(String aiAnswer) {
        this.aiAnswer = aiAnswer;
    }

    public Boolean getAiGenerated() {
        return aiGenerated;
    }

    public void setAiGenerated(Boolean aiGenerated) {
        this.aiGenerated = aiGenerated;
    }

    public Boolean getHumanConfirmed() {
        return humanConfirmed;
    }

    public void setHumanConfirmed(Boolean humanConfirmed) {
        this.humanConfirmed = humanConfirmed;
    }

    public Boolean getReplied() {
        return replied;
    }

    public void setReplied(Boolean replied) {
        this.replied = replied;
    }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
