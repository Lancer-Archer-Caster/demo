package com.jd.tech.stack.study.serverdemo.domain.script.bo;

/**
 * Description: 话术节点BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptNodeBO {

    private Long id;

    private Integer nodeIndex;

    private String content;

    private String keywords;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}