package com.jd.tech.stack.study.serverdemo.domain.ai.bo;

/**
 * Description: 术语翻译BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class JargonTranslateBO {

    private String term;

    private String translation;

    private String category;

    private Boolean aigc;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getAigc() {
        return aigc;
    }

    public void setAigc(Boolean aigc) {
        this.aigc = aigc;
    }
}