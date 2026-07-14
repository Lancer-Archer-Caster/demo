package com.jd.tech.stack.study.serverdemo.client.danmaku.api;

import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuAnswerDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.param.DanmakuProcessParam;

import java.util.List;

/**
 * Description: 弹幕服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface DanmakuService {

    /**
     * 处理弹幕
     */
    DanmakuDTO processDanmaku(DanmakuProcessParam param);

    /**
     * 生成AI回答
     */
    DanmakuAnswerDTO generateAnswer(Long danmakuId);

    /**
     * 确认回答
     */
    Boolean confirmAnswer(Long danmakuId, String answer);

    /**
     * 修改回答
     */
    Boolean modifyAnswer(Long danmakuId, String answer);

    /**
     * 获取弹幕历史
     */
    List<DanmakuDTO> getDanmakuHistory(Long sessionId);
}