package com.jd.tech.stack.study.serverdemo.domain.session.service;

import com.jd.tech.stack.study.serverdemo.domain.session.bo.LiveSessionBO;
import com.jd.tech.stack.study.serverdemo.domain.session.converter.LiveSessionDOConverter;
import com.jd.tech.stack.study.serverdemo.enums.SessionStatusEnum;
import com.jd.tech.stack.study.serverdemo.infra.session.dataobject.LiveSessionDO;
import com.jd.tech.stack.study.serverdemo.infra.session.gateway.LiveSessionGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 直播场次领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class LiveSessionDomainService {

    private static final Logger log = LoggerFactory.getLogger(LiveSessionDomainService.class);

    @Autowired
    private LiveSessionGateway liveSessionGateway;

    public LiveSessionBO createSession(String title, String operator) {
        log.info("createSession title={}", title);
        LiveSessionDO sessionDO = new LiveSessionDO();
        sessionDO.setTitle(title);
        sessionDO.setOperator(operator);
        sessionDO.setStatus(String.valueOf(SessionStatusEnum.PREPARING.getCode()));
        sessionDO = liveSessionGateway.insert(sessionDO);
        return LiveSessionDOConverter.convert(sessionDO);
    }

    public LiveSessionBO getSession(Long sessionId) {
        log.info("getSession id={}", sessionId);
        LiveSessionDO sessionDO = liveSessionGateway.findById(sessionId);
        return LiveSessionDOConverter.convert(sessionDO);
    }

    public LiveSessionBO updateStatus(Long sessionId, Integer status) {
        log.info("updateStatus id={}, status={}", sessionId, status);
        LiveSessionDO sessionDO = liveSessionGateway.findById(sessionId);
        if (sessionDO != null) {
            sessionDO.setStatus(String.valueOf(status));
            liveSessionGateway.update(sessionDO);
        }
        return LiveSessionDOConverter.convert(sessionDO);
    }

    public List<LiveSessionBO> listSessions(String title, String operator) {
        log.info("listSessions title={}", title);
        List<LiveSessionDO> doList = liveSessionGateway.listByParam(title, operator);
        List<LiveSessionBO> boList = new ArrayList<>();
        if (doList != null) {
            for (LiveSessionDO sessionDO : doList) {
                LiveSessionBO bo = LiveSessionDOConverter.convert(sessionDO);
                if (bo != null) {
                    boList.add(bo);
                }
            }
        }
        return boList;
    }

    public Boolean importSkus(Long sessionId, List<String> skuIds) {
        log.info("importSkus sessionId={}, skuCount={}", sessionId, skuIds != null ? skuIds.size() : 0);
        return liveSessionGateway.importSkus(sessionId, skuIds);
    }
}