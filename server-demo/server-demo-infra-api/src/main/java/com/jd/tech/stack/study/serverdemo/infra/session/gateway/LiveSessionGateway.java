package com.jd.tech.stack.study.serverdemo.infra.session.gateway;

import com.jd.tech.stack.study.serverdemo.infra.session.dataobject.LiveSessionDO;

import java.util.List;

/**
 * Description: 直播场次网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface LiveSessionGateway {

    LiveSessionDO insert(LiveSessionDO sessionDO);

    LiveSessionDO findById(Long id);

    List<LiveSessionDO> findByStatus(String status);

    LiveSessionDO update(LiveSessionDO sessionDO);

    List<LiveSessionDO> listByParam(String title, String operator);

    Boolean importSkus(Long sessionId, List<String> skuIds);
}