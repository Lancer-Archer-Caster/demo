package com.jd.tech.stack.study.serverdemo.client.session.api;

import com.jd.tech.stack.study.serverdemo.client.session.dto.LiveSessionDTO;
import com.jd.tech.stack.study.serverdemo.client.session.param.LiveSessionParam;
import com.jd.tech.stack.study.serverdemo.client.session.param.SessionSkuImportParam;

import java.util.List;

/**
 * Description: 直播场次服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface LiveSessionService {

    /**
     * 创建直播场次
     */
    LiveSessionDTO createSession(LiveSessionParam param);

    /**
     * 获取场次详情
     */
    LiveSessionDTO getSession(Long sessionId);

    /**
     * 更新场次状态
     */
    LiveSessionDTO updateStatus(Long sessionId, Integer status);

    /**
     * 查询场次列表
     */
    List<LiveSessionDTO> listSessions(LiveSessionParam param);

    /**
     * 导入商品到场次
     */
    Boolean importSkus(SessionSkuImportParam param);
}