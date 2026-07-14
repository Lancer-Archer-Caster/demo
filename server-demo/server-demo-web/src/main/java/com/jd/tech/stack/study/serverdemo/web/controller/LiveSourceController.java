package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.live.api.LiveSourceService;
import com.jd.tech.stack.study.serverdemo.client.live.dto.LiveSourceConnectionDTO;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveCommentIngestParam;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveSourceConnectParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 直播来源配置与统一评论接入入口。 */
@RestController
@RequestMapping("/live")
public class LiveSourceController {

    @Autowired
    private LiveSourceService liveSourceService;

    @RequestMapping(value = "/source/connect", method = RequestMethod.POST)
    public Result<LiveSourceConnectionDTO> connect(@RequestBody LiveSourceConnectParam param) {
        return new Result<>(liveSourceService.connect(param));
    }

    @RequestMapping(value = "/source/status", method = RequestMethod.GET)
    public Result<LiveSourceConnectionDTO> status(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(liveSourceService.getStatus(sessionId));
    }

    @RequestMapping(value = "/source/disconnect", method = RequestMethod.POST)
    public Result<LiveSourceConnectionDTO> disconnect(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(liveSourceService.disconnect(sessionId));
    }

    @RequestMapping(value = "/comments/ingest", method = RequestMethod.POST)
    public Result<DanmakuDTO> ingest(@RequestBody LiveCommentIngestParam param) {
        return new Result<>(liveSourceService.ingestComment(param));
    }

    @RequestMapping(value = "/comments/demo", method = RequestMethod.POST)
    public Result<DanmakuDTO> demo(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(liveSourceService.ingestDemoComment(sessionId));
    }
}
