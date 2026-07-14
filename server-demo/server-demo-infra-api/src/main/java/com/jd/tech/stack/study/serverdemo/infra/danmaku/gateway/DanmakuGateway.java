package com.jd.tech.stack.study.serverdemo.infra.danmaku.gateway;

import com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject.DanmakuDO;

import java.util.List;

/**
 * Description: 弹幕网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface DanmakuGateway {

    DanmakuDO insert(DanmakuDO danmakuDO);

    DanmakuDO findById(Long id);

    List<DanmakuDO> findBySessionId(Long sessionId);

    DanmakuDO update(DanmakuDO danmakuDO);

    DanmakuDO generateAnswer(Long danmakuId);
}