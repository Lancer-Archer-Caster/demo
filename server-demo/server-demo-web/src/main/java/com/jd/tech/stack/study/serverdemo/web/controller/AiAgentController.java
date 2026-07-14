package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.ai.api.AiAgentService;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.JargonTranslateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.dto.ScriptGenerateDTO;
import com.jd.tech.stack.study.serverdemo.client.ai.param.JargonTranslateParam;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: AI智能体Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/ai")
public class AiAgentController {

    @Autowired
    private AiAgentService aiAgentService;

    @RequestMapping("/translateJargon")
    @ResponseBody
    public Result<JargonTranslateDTO> translateJargon(@RequestBody JargonTranslateParam param) {
        return new Result<>(aiAgentService.translateJargon(param));
    }

    @RequestMapping("/generateScriptContent")
    @ResponseBody
    public Result<ScriptGenerateDTO> generateScriptContent(@RequestBody ScriptGenerateParam param) {
        return new Result<>(aiAgentService.generateScriptContent(param));
    }

    @RequestMapping("/classifyIntent")
    @ResponseBody
    public Result<String> classifyIntent(@RequestParam("text") String text) {
        return new Result<>(aiAgentService.classifyIntent(text));
    }

    @RequestMapping("/goldenSentences")
    @ResponseBody
    public Result<List<String>> extractGoldenSentences(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(aiAgentService.extractGoldenSentences(sessionId));
    }
}