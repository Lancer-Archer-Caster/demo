package com.jd.tech.stack.study.serverdemo.infra.script.dataobject;

/**
 * Description: 脚本节点DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptNodeDO {

    private Long id;

    private Long scriptId;

    private Integer nodeIndex;

    private String nodeType;

    private String content;

    private String keywords;

    private Integer sortOrder;

    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getScriptId() { return scriptId; }
    public void setScriptId(Long scriptId) { this.scriptId = scriptId; }

    public Integer getNodeIndex() { return nodeIndex; }
    public void setNodeIndex(Integer nodeIndex) { this.nodeIndex = nodeIndex; }

    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}