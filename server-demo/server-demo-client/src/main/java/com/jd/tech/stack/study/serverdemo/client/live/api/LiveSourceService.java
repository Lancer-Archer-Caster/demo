package com.jd.tech.stack.study.serverdemo.client.live.api;

import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.live.dto.LiveSourceConnectionDTO;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveCommentIngestParam;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveSourceConnectParam;

/** 直播来源与评论标准接入服务。 */
public interface LiveSourceService {

    LiveSourceConnectionDTO connect(LiveSourceConnectParam param);

    LiveSourceConnectionDTO getStatus(Long sessionId);

    LiveSourceConnectionDTO disconnect(Long sessionId);

    DanmakuDTO ingestComment(LiveCommentIngestParam param);

    DanmakuDTO ingestDemoComment(Long sessionId);
}
