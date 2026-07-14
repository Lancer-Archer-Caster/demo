package com.jd.tech.stack.study.serverdemo.domain.ai.service;

import com.jd.tech.stack.study.serverdemo.domain.ai.bo.JargonTranslateBO;
import com.jd.tech.stack.study.serverdemo.domain.ai.bo.ScriptGenerateBO;
import com.jd.tech.stack.study.serverdemo.domain.ai.converter.AiAgentDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.JargonTranslateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.ScriptGenerateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.gateway.AiAgentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: AI智能体领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AiAgentDomainService {

    private static final Logger log = LoggerFactory.getLogger(AiAgentDomainService.class);

    @Autowired
    private AiAgentGateway aiAgentGateway;

    public JargonTranslateBO translateJargon(String term) {
        log.info("translateJargon term={}", term);
        // TODO: AI术语翻译逻辑，目前仅查询已有记录
        JargonTranslateDO jargonDO = aiAgentGateway.findJargonByJargon(term);
        return AiAgentDOConverter.convert(jargonDO);
    }

    public ScriptGenerateBO generateScriptContent(Long sessionId) {
        log.info("generateScriptContent sessionId={}", sessionId);
        // TODO: AI脚本生成逻辑，目前仅查询已有记录
        ScriptGenerateDO scriptDO = aiAgentGateway.findScriptBySessionId(sessionId);
        return AiAgentDOConverter.convert(scriptDO);
    }

    public String classifyIntent(String text) {
        log.info("classifyIntent text={}", text);
        return aiAgentGateway.classifyIntent(text);
    }

    public List<String> extractGoldenSentences(Long sessionId) {
        log.info("extractGoldenSentences sessionId={}", sessionId);
        return aiAgentGateway.extractGoldenSentences(sessionId);
    }
}