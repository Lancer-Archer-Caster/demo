package com.jd.tech.stack.study.serverdemo.infra.danmaku.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject.DanmakuDO;
import com.jd.tech.stack.study.serverdemo.infra.danmaku.gateway.DanmakuGateway;
import com.jd.tech.stack.study.serverdemo.infra.danmaku.mapper.DanmakuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 弹幕网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class DanmakuGatewayImpl implements DanmakuGateway {

    @Autowired
    private DanmakuMapper danmakuMapper;

    @Override
    public DanmakuDO insert(DanmakuDO danmakuDO) {
        danmakuMapper.insert(danmakuDO);
        return danmakuDO;
    }

    @Override
    public DanmakuDO findById(Long id) {
        return danmakuMapper.getById(id);
    }

    @Override
    public List<DanmakuDO> findBySessionId(Long sessionId) {
        return danmakuMapper.getBySessionId(sessionId);
    }

    @Override
    public DanmakuDO update(DanmakuDO danmakuDO) {
        danmakuMapper.update(danmakuDO);
        return danmakuDO;
    }

    @Override
    public DanmakuDO generateAnswer(Long danmakuId) {
        DanmakuDO danmakuDO = danmakuMapper.getById(danmakuId);
        if (danmakuDO != null) {
            danmakuDO.setAigc(false);
            danmakuDO.setAnswer("本地知识库暂未命中，请主播结合商品详情口播回答。");
            danmakuMapper.update(danmakuDO);
        }
        return danmakuDO;
    }
}
