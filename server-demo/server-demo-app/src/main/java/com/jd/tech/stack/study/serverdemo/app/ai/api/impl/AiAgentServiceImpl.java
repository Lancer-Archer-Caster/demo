package com.jd.tech.stack.study.serverdemo.app.ai.api.impl;

import com.jd.tech.stack.study.serverdemo.app.ai.converter.AiAgentBOConverter;
import com.jd.tech.stack.study.serverdemo.client.ai.api.AiAgentService;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.JargonTranslateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.ScriptGenerateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.param.JargonTranslateParam;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;
import com.jd.tech.stack.study.serverdemo.domain.ai.service.AiAgentDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: AI智能体服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AiAgentServiceImpl implements AiAgentService {

    private static final Logger log = LoggerFactory.getLogger(AiAgentServiceImpl.class);

    @Autowired
    private AiAgentDomainService aiAgentDomainService;

    @Override
    public JargonTranslateDTO translateJargon(JargonTranslateParam param) {
        log.info("translateJargon term={}", param.getTerm());
        return AiAgentBOConverter.convert(aiAgentDomainService.translateJargon(param.getTerm()));
    }

    @Override
    public ScriptGenerateDTO generateScriptContent(ScriptGenerateParam param) {
        log.info("generateScriptContent sessionId={}", param.getSessionId());
        return AiAgentBOConverter.convert(aiAgentDomainService.generateScriptContent(param.getSessionId()));
    }

    @Override
    public String classifyIntent(String text) {
        log.info("classifyIntent text={}", text);
        return aiAgentDomainService.classifyIntent(text);
    }

    @Override
    public List<String> extractGoldenSentences(Long sessionId) {
        log.info("extractGoldenSentences sessionId={}", sessionId);
        return aiAgentDomainService.extractGoldenSentences(sessionId);
    }
}