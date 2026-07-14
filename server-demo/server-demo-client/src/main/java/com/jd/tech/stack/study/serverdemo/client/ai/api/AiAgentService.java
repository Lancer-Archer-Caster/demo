package com.jd.tech.stack.study.serverdemo.client.ai.api;

import com.jd.tech.stack.study.serverdemo.client.ai.dto.JargonTranslateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.ScriptGenerateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.param.JargonTranslateParam;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;

import java.util.List;

/**
 * Description: AI智能体服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface AiAgentService {

    /**
     * 术语翻译
     */
    JargonTranslateDTO translateJargon(JargonTranslateParam param);

    /**
     * 生成脚本内容
     */
    ScriptGenerateDTO generateScriptContent(ScriptGenerateParam param);

    /**
     * 意图分类
     */
    String classifyIntent(String text);

    /**
     * 提取金句
     */
    List<String> extractGoldenSentences(Long sessionId);
}