package com.jd.tech.stack.study.serverdemo.domain.script.bo;

import java.util.List;

/**
 * Description: 话术BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptBO {

    private Long id;

    private Long sessionId;

    private String skuId;

    private String content;

    private Boolean aiGenerated;

    private List<ScriptNodeBO> nodes;

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

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getAiGenerated() {
        return aiGenerated;
    }

    public void setAiGenerated(Boolean aiGenerated) {
        this.aiGenerated = aiGenerated;
    }

    public List<ScriptNodeBO> getNodes() {
        return nodes;
    }

    public void setNodes(List<ScriptNodeBO> nodes) {
        this.nodes = nodes;
    }
}