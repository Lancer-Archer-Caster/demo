package com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject;

/**
 * Description: 合规检查结果DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ComplianceResultDO {

    private Long id;

    private Long sessionId;

    private Long skuId;

    private String skuName;

    private String checkContent;

    private Boolean passed;

    private Integer hitCount;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public String getSkuName() { return skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }

    public String getCheckContent() { return checkContent; }
    public void setCheckContent(String checkContent) { this.checkContent = checkContent; }

    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }

    public Integer getHitCount() { return hitCount; }
    public void setHitCount(Integer hitCount) { this.hitCount = hitCount; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}