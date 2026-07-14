package com.jd.tech.stack.study.serverdemo.infra.ai.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.JargonTranslateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.ScriptGenerateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.gateway.AiAgentGateway;
import com.jd.tech.stack.study.serverdemo.infra.ai.mapper.JargonTranslateMapper;
import com.jd.tech.stack.study.serverdemo.infra.ai.mapper.ScriptGenerateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: AI智能体网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AiAgentGatewayImpl implements AiAgentGateway {

    @Autowired
    private JargonTranslateMapper jargonTranslateMapper;

    @Autowired
    private ScriptGenerateMapper scriptGenerateMapper;

    @Override
    public JargonTranslateDO findJargonByJargon(String jargon) {
        return jargonTranslateMapper.getByJargon(jargon);
    }

    @Override
    public JargonTranslateDO insertJargon(JargonTranslateDO jargonTranslateDO) {
        jargonTranslateMapper.insert(jargonTranslateDO);
        return jargonTranslateDO;
    }

    @Override
    public ScriptGenerateDO findScriptBySessionId(Long sessionId) {
        return scriptGenerateMapper.getBySessionId(sessionId);
    }

    @Override
    public ScriptGenerateDO insertScript(ScriptGenerateDO scriptGenerateDO) {
        scriptGenerateMapper.insert(scriptGenerateDO);
        return scriptGenerateDO;
    }

    @Override
    public String classifyIntent(String text) {
        // TODO: 调用AI意图分类模型
        return "GENERAL";
    }

    @Override
    public List<String> extractGoldenSentences(Long sessionId) {
        // TODO: 调用AI提取金句
        return new ArrayList<>();
    }
}