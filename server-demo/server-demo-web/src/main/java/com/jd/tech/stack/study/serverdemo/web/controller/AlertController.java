package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.alert.api.AlertService;
import com.jd.tech.stack.study.serverdemo.client.alert.dto.AlertDTO;
import com.jd.tech.stack.study.serverdemo.client.alert.param.AlertSubscribeParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 告警Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @RequestMapping("/subscribe")
    @ResponseBody
    public Result<Boolean> subscribeAlerts(@RequestBody AlertSubscribeParam param) {
        return new Result<>(alertService.subscribeAlerts(param));
    }

    @RequestMapping("/active")
    @ResponseBody
    public Result<List<AlertDTO>> getActiveAlerts(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(alertService.getActiveAlerts(sessionId));
    }

    @RequestMapping("/dismiss")
    @ResponseBody
    public Result<Boolean> dismissAlert(@RequestParam("alertId") Long alertId) {
        return new Result<>(alertService.dismissAlert(alertId));
    }

    @RequestMapping("/history")
    @ResponseBody
    public Result<List<AlertDTO>> getAlertHistory(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(alertService.getAlertHistory(sessionId));
    }
}