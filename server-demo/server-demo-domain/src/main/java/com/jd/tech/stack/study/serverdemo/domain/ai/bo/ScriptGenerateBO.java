package com.jd.tech.stack.study.serverdemo.domain.ai.bo;

/**
 * Description: 脚本生成BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptGenerateBO {

    private String content;

    private String thinking;

    private Boolean aigc;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThinking() {
        return thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public Boolean getAigc() {
        return aigc;
    }

    public void setAigc(Boolean aigc) {
        this.aigc = aigc;
    }
}