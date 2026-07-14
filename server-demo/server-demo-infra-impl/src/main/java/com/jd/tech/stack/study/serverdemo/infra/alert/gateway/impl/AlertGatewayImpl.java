package com.jd.tech.stack.study.serverdemo.infra.alert.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.alert.dataobject.AlertDO;
import com.jd.tech.stack.study.serverdemo.infra.alert.gateway.AlertGateway;
import com.jd.tech.stack.study.serverdemo.infra.alert.mapper.AlertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 告警网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class AlertGatewayImpl implements AlertGateway {

    @Autowired
    private AlertMapper alertMapper;

    @Override
    public AlertDO insert(AlertDO alertDO) {
        alertMapper.insert(alertDO);
        return alertDO;
    }

    @Override
    public AlertDO findById(Long id) {
        return alertMapper.getById(id);
    }

    @Override
    public List<AlertDO> findBySessionId(Long sessionId) {
        return alertMapper.getBySessionId(sessionId);
    }

    @Override
    public AlertDO update(AlertDO alertDO) {
        alertMapper.update(alertDO);
        return alertDO;
    }

    @Override
    public Boolean subscribeAlerts(Long sessionId, List<String> alertTypes) {
        // TODO: 实现告警订阅逻辑
        for (String alertType : alertTypes) {
            AlertDO alertDO = new AlertDO();
            alertDO.setSessionId(sessionId);
            alertDO.setAlertType(alertType);
            alertDO.setLevel("INFO");
            alertDO.setMessage("订阅告警: " + alertType);
            alertMapper.insert(alertDO);
        }
        return true;
    }

    @Override
    public List<AlertDO> getActiveAlerts(Long sessionId) {
        return alertMapper.getActiveBySessionId(sessionId);
    }

    @Override
    public Boolean dismissAlert(Long alertId) {
        AlertDO alertDO = alertMapper.getById(alertId);
        if (alertDO != null) {
            alertDO.setHandled("1");
            alertMapper.update(alertDO);
            return true;
        }
        return false;
    }

    @Override
    public List<AlertDO> getAlertHistory(Long sessionId) {
        return alertMapper.getHistoryBySessionId(sessionId);
    }
}