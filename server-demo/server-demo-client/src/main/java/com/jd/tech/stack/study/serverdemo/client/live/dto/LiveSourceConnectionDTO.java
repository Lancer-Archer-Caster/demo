package com.jd.tech.stack.study.serverdemo.client.live.dto;

import lombok.Data;

/** 直播来源配置。真实平台适配器与本地 webhook 共用此状态模型。 */
@Data
public class LiveSourceConnectionDTO {

    private Long sessionId;
    private String roomUrl;
    private String platform;
    private String status;
    private String connectionMode;
    private String connectedAt;
    private String commentWebhook;
    private String message;
}
