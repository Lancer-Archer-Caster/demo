package com.jd.tech.stack.study.serverdemo.infra.session.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.session.dataobject.LiveSessionDO;
import com.jd.tech.stack.study.serverdemo.infra.session.gateway.LiveSessionGateway;
import com.jd.tech.stack.study.serverdemo.infra.session.mapper.LiveSessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 直播场次网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class LiveSessionGatewayImpl implements LiveSessionGateway {

    @Autowired
    private LiveSessionMapper liveSessionMapper;

    @Override
    public LiveSessionDO insert(LiveSessionDO sessionDO) {
        liveSessionMapper.insert(sessionDO);
        return sessionDO;
    }

    @Override
    public LiveSessionDO findById(Long id) {
        return liveSessionMapper.getById(id);
    }

    @Override
    public List<LiveSessionDO> findByStatus(String status) {
        return liveSessionMapper.getByStatus(status);
    }

    @Override
    public LiveSessionDO update(LiveSessionDO sessionDO) {
        liveSessionMapper.update(sessionDO);
        return sessionDO;
    }

    @Override
    public List<LiveSessionDO> listByParam(String title, String operator) {
        // TODO: 根据参数动态查询
        return liveSessionMapper.getAll();
    }

    @Override
    public Boolean importSkus(Long sessionId, List<String> skuIds) {
        // TODO: 实现导入SKU逻辑
        return true;
    }
}