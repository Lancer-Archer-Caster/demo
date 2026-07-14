package com.jd.tech.stack.study.serverdemo.infra.ai.gateway;

import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.JargonTranslateDO;
import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.ScriptGenerateDO;

import java.util.List;

/**
 * Description: AI智能体网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface AiAgentGateway {

    JargonTranslateDO findJargonByJargon(String jargon);

    JargonTranslateDO insertJargon(JargonTranslateDO jargonTranslateDO);

    ScriptGenerateDO findScriptBySessionId(Long sessionId);

    ScriptGenerateDO insertScript(ScriptGenerateDO scriptGenerateDO);

    String classifyIntent(String text);

    List<String> extractGoldenSentences(Long sessionId);
}