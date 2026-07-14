package com.jd.tech.stack.study.serverdemo.client.alert.param;

import lombok.Data;

import java.util.List;

/**
 * Description: 告警订阅参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class AlertSubscribeParam {

    /** 场次ID */
    private Long sessionId;

    /** 告警类型列表 */
    private List<String> alertTypes;
}