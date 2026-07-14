package com.jd.tech.stack.study.serverdemo.domain.ai.converter;

import com.jd.tech.stack.study.serverdemo.domain.ai.bo.JargonTranslateBO;
import com.jd.tech.stack.study.serverdemo.domain.ai.bo.ScriptGenerateBO;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.JargonTranslateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.ScriptGenerateDO;

/**
 * Description: AI智能体DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class AiAgentDOConverter {

    public static JargonTranslateBO convert(JargonTranslateDO jargonDO) {
        if (jargonDO == null) {
            return null;
        }
        JargonTranslateBO bo = new JargonTranslateBO();
        bo.setTerm(jargonDO.getJargon());
        bo.setTranslation(jargonDO.getTranslation());
        bo.setCategory(jargonDO.getCategory());
        bo.setAigc(jargonDO.getAigc());
        return bo;
    }

    public static ScriptGenerateBO convert(ScriptGenerateDO scriptDO) {
        if (scriptDO == null) {
            return null;
        }
        ScriptGenerateBO bo = new ScriptGenerateBO();
        bo.setContent(scriptDO.getContent());
        bo.setThinking(null); // ScriptGenerateDO无thinking字段
        bo.setAigc(scriptDO.getAigc());
        return bo;
    }
}