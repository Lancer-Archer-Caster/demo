package com.jd.tech.stack.study.serverdemo.app.live.api.impl;

import com.jd.tech.stack.study.serverdemo.client.danmaku.api.DanmakuService;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.param.DanmakuProcessParam;
import com.jd.tech.stack.study.serverdemo.client.live.api.LiveSourceService;
import com.jd.tech.stack.study.serverdemo.client.live.dto.LiveSourceConnectionDTO;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveCommentIngestParam;
import com.jd.tech.stack.study.serverdemo.client.live.param.LiveSourceConnectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地实现只维护来源状态与标准评论入口。
 * 真正接抖音/快手等平台时，实现平台适配器并调用 ingestComment 即可。
 */
@Service
public class LiveSourceServiceImpl implements LiveSourceService {

    private static final String[] DEMO_COMMENTS = {
            "这款手机支持快充吗？",
            "屏幕刷新率是多少？",
            "666",
            "支持NFC吗？",
            "电池续航怎么样？"
    };

    private final ConcurrentMap<Long, LiveSourceConnectionDTO> connections = new ConcurrentHashMap<>();
    private final AtomicInteger demoIndex = new AtomicInteger();

    @Autowired
    private DanmakuService danmakuService;

    @Override
    public LiveSourceConnectionDTO connect(LiveSourceConnectParam param) {
        if (param == null || param.getSessionId() == null) {
            throw new IllegalArgumentException("sessionId不能为空");
        }
        validateRoomUrl(param.getRoomUrl());
        LiveSourceConnectionDTO connection = new LiveSourceConnectionDTO();
        connection.setSessionId(param.getSessionId());
        connection.setRoomUrl(param.getRoomUrl().trim());
        connection.setPlatform(empty(param.getPlatform()) ? detectPlatform(param.getRoomUrl()) : param.getPlatform());
        connection.setStatus("READY");
        connection.setConnectionMode("WEBHOOK_ADAPTER");
        connection.setConnectedAt(String.valueOf(System.currentTimeMillis()));
        connection.setCommentWebhook("/live/comments/ingest");
        connection.setMessage("直播来源已登记；平台适配器应把评论推送到统一评论接口。");
        connections.put(param.getSessionId(), connection);
        return connection;
    }

    @Override
    public LiveSourceConnectionDTO getStatus(Long sessionId) {
        LiveSourceConnectionDTO current = connections.get(sessionId);
        return current != null ? current : disconnected(sessionId);
    }

    @Override
    public LiveSourceConnectionDTO disconnect(Long sessionId) {
        LiveSourceConnectionDTO current = connections.remove(sessionId);
        LiveSourceConnectionDTO result = current != null ? current : disconnected(sessionId);
        result.setStatus("DISCONNECTED");
        result.setMessage("直播来源已断开");
        return result;
    }

    @Override
    public DanmakuDTO ingestComment(LiveCommentIngestParam param) {
        if (param == null || param.getSessionId() == null || empty(param.getText())) {
            throw new IllegalArgumentException("sessionId和评论text不能为空");
        }
        DanmakuProcessParam processParam = new DanmakuProcessParam();
        processParam.setSessionId(param.getSessionId());
        processParam.setUserId(empty(param.getUserId()) ? "匿名观众" : param.getUserId());
        processParam.setText(param.getText().trim());
        return danmakuService.processDanmaku(processParam);
    }

    @Override
    public DanmakuDTO ingestDemoComment(Long sessionId) {
        int index = Math.floorMod(demoIndex.getAndIncrement(), DEMO_COMMENTS.length);
        LiveCommentIngestParam param = new LiveCommentIngestParam();
        param.setSessionId(sessionId);
        param.setUserId("本地观众" + (index + 1));
        param.setText(DEMO_COMMENTS[index]);
        param.setTimestamp(System.currentTimeMillis());
        param.setSource("LOCAL_DEMO");
        return ingestComment(param);
    }

    private LiveSourceConnectionDTO disconnected(Long sessionId) {
        LiveSourceConnectionDTO connection = new LiveSourceConnectionDTO();
        connection.setSessionId(sessionId);
        connection.setStatus("DISCONNECTED");
        connection.setConnectionMode("WEBHOOK_ADAPTER");
        connection.setCommentWebhook("/live/comments/ingest");
        connection.setMessage("尚未登记直播来源");
        return connection;
    }

    private void validateRoomUrl(String roomUrl) {
        if (empty(roomUrl)) {
            throw new IllegalArgumentException("直播间链接不能为空");
        }
        try {
            URI uri = URI.create(roomUrl.trim());
            String scheme = uri.getScheme();
            if (!("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))) {
                throw new IllegalArgumentException("直播间链接仅支持http或https");
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("直播间链接格式不正确");
        }
    }

    private String detectPlatform(String roomUrl) {
        String value = roomUrl.toLowerCase(Locale.ROOT);
        if (value.contains("douyin")) return "DOUYIN";
        if (value.contains("kuaishou")) return "KUAISHOU";
        if (value.contains("taobao") || value.contains("tmall")) return "TAOBAO";
        if (value.contains("jd.com")) return "JD_LIVE";
        return "CUSTOM";
    }

    private boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
