package com.jd.tech.stack.study.serverdemo.client.live.param;

import lombok.Data;

/** 登记直播间来源。URL 本身不负责抓评论，评论由平台适配器推送到统一入口。 */
@Data
public class LiveSourceConnectParam {

    private Long sessionId;
    private String roomUrl;
    private String platform;
}
