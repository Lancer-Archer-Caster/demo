package com.jd.tech.stack.study.serverdemo.app.ai.converter;

import com.jd.tech.stack.study.serverdemo.client.ai.dto.JargonTranslateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.ScriptGenerateDTO;
import com.jd.tech.stack.study.serverdemo.domain.ai.bo.JargonTranslateBO;
import com.jd.tech.stack.study.serverdemo.domain.ai.bo.ScriptGenerateBO;

/**
 * Description: AI智能体BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class AiAgentBOConverter {

    public static JargonTranslateDTO convert(JargonTranslateBO bo) {
        if (bo == null) {
            return null;
        }
        JargonTranslateDTO dto = new JargonTranslateDTO();
        dto.setTerm(bo.getTerm());
        dto.setTranslation(bo.getTranslation());
        dto.setCategory(bo.getCategory());
        dto.setAigc(bo.getAigc());
        return dto;
    }

    public static ScriptGenerateDTO convert(ScriptGenerateBO bo) {
        if (bo == null) {
            return null;
        }
        ScriptGenerateDTO dto = new ScriptGenerateDTO();
        dto.setContent(bo.getContent());
        dto.setThinking(bo.getThinking());
        dto.setAigc(bo.getAigc());
        return dto;
    }
}