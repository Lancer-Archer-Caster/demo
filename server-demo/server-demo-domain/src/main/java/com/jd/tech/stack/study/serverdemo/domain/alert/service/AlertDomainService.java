package com.jd.tech.stack.study.serverdemo.domain.alert.service;

import com.jd.tech.stack.study.serverdemo.domain.alert.bo.AlertBO;
import com.jd.tech.stack.study.serverdemo.domain.alert.converter.AlertDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.alert.dataobject.AlertDO;
import com.jd.tech.stack.study.serverdemo.infra.alert.gateway.AlertGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 告警领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AlertDomainService {

    private static final Logger log = LoggerFactory.getLogger(AlertDomainService.class);

    @Autowired
    private AlertGateway alertGateway;

    public Boolean subscribeAlerts(Long sessionId, List<String> alertTypes) {
        log.info("subscribeAlerts sessionId={}", sessionId);
        return alertGateway.subscribeAlerts(sessionId, alertTypes);
    }

    public List<AlertBO> getActiveAlerts(Long sessionId) {
        log.info("getActiveAlerts sessionId={}", sessionId);
        List<AlertDO> doList = alertGateway.getActiveAlerts(sessionId);
        return AlertDOConverter.convertList(doList);
    }

    public Boolean dismissAlert(Long alertId) {
        log.info("dismissAlert alertId={}", alertId);
        return alertGateway.dismissAlert(alertId);
    }

    public List<AlertBO> getAlertHistory(Long sessionId) {
        log.info("getAlertHistory sessionId={}", sessionId);
        List<AlertDO> doList = alertGateway.getAlertHistory(sessionId);
        return AlertDOConverter.convertList(doList);
    }
}