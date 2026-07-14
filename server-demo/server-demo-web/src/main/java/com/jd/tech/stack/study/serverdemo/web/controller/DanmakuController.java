package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.danmaku.api.DanmakuService;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuAnswerDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.param.DanmakuProcessParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 弹幕Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/danmaku")
public class DanmakuController {

    @Autowired
    private DanmakuService danmakuService;

    @RequestMapping("/process")
    @ResponseBody
    public Result<DanmakuDTO> processDanmaku(@RequestBody DanmakuProcessParam param) {
        return new Result<>(danmakuService.processDanmaku(param));
    }

    @RequestMapping("/generateAnswer")
    @ResponseBody
    public Result<DanmakuAnswerDTO> generateAnswer(@RequestParam("danmakuId") Long danmakuId) {
        return new Result<>(danmakuService.generateAnswer(danmakuId));
    }

    @RequestMapping("/confirmAnswer")
    @ResponseBody
    public Result<Boolean> confirmAnswer(@RequestParam("danmakuId") Long danmakuId,
                                          @RequestParam("answer") String answer) {
        return new Result<>(danmakuService.confirmAnswer(danmakuId, answer));
    }

    @RequestMapping("/modifyAnswer")
    @ResponseBody
    public Result<Boolean> modifyAnswer(@RequestParam("danmakuId") Long danmakuId,
                                         @RequestParam("answer") String answer) {
        return new Result<>(danmakuService.modifyAnswer(danmakuId, answer));
    }

    @RequestMapping("/history")
    @ResponseBody
    public Result<List<DanmakuDTO>> getDanmakuHistory(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(danmakuService.getDanmakuHistory(sessionId));
    }
}