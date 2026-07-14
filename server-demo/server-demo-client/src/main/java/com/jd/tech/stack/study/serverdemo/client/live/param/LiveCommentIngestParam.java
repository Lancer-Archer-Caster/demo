package com.jd.tech.stack.study.serverdemo.client.live.param;

import lombok.Data;

/** 平台适配器转换后的统一评论模型。 */
@Data
public class LiveCommentIngestParam {

    private Long sessionId;
    private String userId;
    private String text;
    private Long timestamp;
    private String source;
}
