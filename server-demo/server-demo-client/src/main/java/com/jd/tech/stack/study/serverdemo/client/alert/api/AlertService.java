package com.jd.tech.stack.study.serverdemo.client.alert.api;

import com.jd.tech.stack.study.serverdemo.client.alert.dto.AlertDTO;
import com.jd.tech.stack.study.serverdemo.client.alert.param.AlertSubscribeParam;

import java.util.List;

/**
 * Description: 告警服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface AlertService {

    /**
     * 订阅告警
     */
    Boolean subscribeAlerts(AlertSubscribeParam param);

    /**
     * 获取活跃告警
     */
    List<AlertDTO> getActiveAlerts(Long sessionId);

    /**
     * 忽略告警
     */
    Boolean dismissAlert(Long alertId);

    /**
     * 获取告警历史
     */
    List<AlertDTO> getAlertHistory(Long sessionId);
}