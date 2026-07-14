package com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject;

/**
 * Description: 弹幕DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class DanmakuDO {

    private Long id;

    private Long sessionId;

    private String content;

    private String senderNick;

    private String intent;

    private String answer;

    private Boolean aigc;

    private Boolean humanConfirmed;

    private Boolean replied;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSenderNick() { return senderNick; }
    public void setSenderNick(String senderNick) { this.senderNick = senderNick; }

    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public Boolean getAigc() { return aigc; }
    public void setAigc(Boolean aigc) { this.aigc = aigc; }

    public Boolean getHumanConfirmed() { return humanConfirmed; }
    public void setHumanConfirmed(Boolean humanConfirmed) { this.humanConfirmed = humanConfirmed; }

    public Boolean getReplied() { return replied; }
    public void setReplied(Boolean replied) { this.replied = replied; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}