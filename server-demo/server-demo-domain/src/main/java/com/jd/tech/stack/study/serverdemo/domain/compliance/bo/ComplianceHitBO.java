package com.jd.tech.stack.study.serverdemo.domain.compliance.bo;

/**
 * Description: 合规命中项BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ComplianceHitBO {

    private Long id;

    private Long resultId;

    private String category;

    private String ruleName;

    private String hitContent;

    private String suggestion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getHitContent() {
        return hitContent;
    }

    public void setHitContent(String hitContent) {
        this.hitContent = hitContent;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}