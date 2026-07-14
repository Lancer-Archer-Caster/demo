package com.jd.tech.stack.study.serverdemo.app.alert.api.impl;

import com.jd.tech.stack.study.serverdemo.app.alert.converter.AlertBOConverter;
import com.jd.tech.stack.study.serverdemo.client.alert.api.AlertService;
import com.jd.tech.stack.study.serverdemo.client.alert.dto.AlertDTO;
import com.jd.tech.stack.study.serverdemo.client.alert.param.AlertSubscribeParam;
import com.jd.tech.stack.study.serverdemo.domain.alert.bo.AlertBO;
import com.jd.tech.stack.study.serverdemo.domain.alert.service.AlertDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 告警服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertServiceImpl.class);

    @Autowired
    private AlertDomainService alertDomainService;

    @Override
    public Boolean subscribeAlerts(AlertSubscribeParam param) {
        log.info("subscribeAlerts sessionId={}", param.getSessionId());
        return alertDomainService.subscribeAlerts(param.getSessionId(), param.getAlertTypes());
    }

    @Override
    public List<AlertDTO> getActiveAlerts(Long sessionId) {
        log.info("getActiveAlerts sessionId={}", sessionId);
        List<AlertBO> boList = alertDomainService.getActiveAlerts(sessionId);
        return AlertBOConverter.convertList(boList);
    }

    @Override
    public Boolean dismissAlert(Long alertId) {
        log.info("dismissAlert alertId={}", alertId);
        return alertDomainService.dismissAlert(alertId);
    }

    @Override
    public List<AlertDTO> getAlertHistory(Long sessionId) {
        log.info("getAlertHistory sessionId={}", sessionId);
        List<AlertBO> boList = alertDomainService.getAlertHistory(sessionId);
        return AlertBOConverter.convertList(boList);
    }
}