package com.jd.tech.stack.study.serverdemo.infra.alert.gateway;

import com.jd.tech.stack.study.serverdemo.infra.alert.dataobject.AlertDO;

import java.util.List;

/**
 * Description: 告警网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface AlertGateway {

    AlertDO insert(AlertDO alertDO);

    AlertDO findById(Long id);

    List<AlertDO> findBySessionId(Long sessionId);

    AlertDO update(AlertDO alertDO);

    Boolean subscribeAlerts(Long sessionId, List<String> alertTypes);

    List<AlertDO> getActiveAlerts(Long sessionId);

    Boolean dismissAlert(Long alertId);

    List<AlertDO> getAlertHistory(Long sessionId);
}