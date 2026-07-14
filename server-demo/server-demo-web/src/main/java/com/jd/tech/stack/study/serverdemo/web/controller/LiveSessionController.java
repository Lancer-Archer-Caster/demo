package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.session.api.LiveSessionService;
import com.jd.tech.stack.study.serverdemo.client.session.dto.LiveSessionDTO;
import com.jd.tech.stack.study.serverdemo.client.session.param.LiveSessionParam;
import com.jd.tech.stack.study.serverdemo.client.session.param.SessionSkuImportParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 直播场次Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/session")
public class LiveSessionController {

    @Autowired
    private LiveSessionService liveSessionService;

    @RequestMapping("/create")
    @ResponseBody
    public Result<LiveSessionDTO> createSession(@RequestBody LiveSessionParam param) {
        return new Result<>(liveSessionService.createSession(param));
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<LiveSessionDTO> getSession(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(liveSessionService.getSession(sessionId));
    }

    @RequestMapping("/updateStatus")
    @ResponseBody
    public Result<LiveSessionDTO> updateStatus(@RequestParam("sessionId") Long sessionId,
                                                @RequestParam("status") Integer status) {
        return new Result<>(liveSessionService.updateStatus(sessionId, status));
    }

    @RequestMapping("/list")
    @ResponseBody
    public Result<List<LiveSessionDTO>> listSessions(@RequestBody LiveSessionParam param) {
        return new Result<>(liveSessionService.listSessions(param));
    }

    @RequestMapping("/importSkus")
    @ResponseBody
    public Result<Boolean> importSkus(@RequestBody SessionSkuImportParam param) {
        return new Result<>(liveSessionService.importSkus(param));
    }
}