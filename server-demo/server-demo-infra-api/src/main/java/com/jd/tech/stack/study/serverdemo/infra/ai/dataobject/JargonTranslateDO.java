package com.jd.tech.stack.study.serverdemo.infra.ai.dataobject;

/**
 * Description: 术语翻译DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class JargonTranslateDO {

    private Long id;

    private String jargon;

    private String translation;

    private String category;

    private Boolean aigc;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJargon() { return jargon; }
    public void setJargon(String jargon) { this.jargon = jargon; }

    public String getTranslation() { return translation; }
    public void setTranslation(String translation) { this.translation = translation; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Boolean getAigc() { return aigc; }
    public void setAigc(Boolean aigc) { this.aigc = aigc; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}