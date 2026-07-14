package com.jd.tech.stack.study.serverdemo.app.session.api.impl;

import com.jd.tech.stack.study.serverdemo.app.session.converter.LiveSessionBOConverter;
import com.jd.tech.stack.study.serverdemo.client.session.api.LiveSessionService;
import com.jd.tech.stack.study.serverdemo.client.session.dto.LiveSessionDTO;
import com.jd.tech.stack.study.serverdemo.client.session.param.LiveSessionParam;
import com.jd.tech.stack.study.serverdemo.client.session.param.SessionSkuImportParam;
import com.jd.tech.stack.study.serverdemo.domain.session.bo.LiveSessionBO;
import com.jd.tech.stack.study.serverdemo.domain.session.service.LiveSessionDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 场次服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class LiveSessionServiceImpl implements LiveSessionService {

    private static final Logger log = LoggerFactory.getLogger(LiveSessionServiceImpl.class);

    @Autowired
    private LiveSessionDomainService liveSessionDomainService;

    @Override
    public LiveSessionDTO createSession(LiveSessionParam param) {
        log.info("createSession title={}", param.getTitle());
        LiveSessionBO bo = liveSessionDomainService.createSession(param.getTitle(), param.getOperator());
        return LiveSessionBOConverter.convert(bo);
    }

    @Override
    public LiveSessionDTO getSession(Long sessionId) {
        log.info("getSession id={}", sessionId);
        LiveSessionBO bo = liveSessionDomainService.getSession(sessionId);
        return LiveSessionBOConverter.convert(bo);
    }

    @Override
    public LiveSessionDTO updateStatus(Long sessionId, Integer status) {
        log.info("updateStatus id={}, status={}", sessionId, status);
        LiveSessionBO bo = liveSessionDomainService.updateStatus(sessionId, status);
        return LiveSessionBOConverter.convert(bo);
    }

    @Override
    public List<LiveSessionDTO> listSessions(LiveSessionParam param) {
        log.info("listSessions title={}", param.getTitle());
        List<LiveSessionBO> boList = liveSessionDomainService.listSessions(param.getTitle(), param.getOperator());
        return LiveSessionBOConverter.convertList(boList);
    }

    @Override
    public Boolean importSkus(SessionSkuImportParam param) {
        log.info("importSkus sessionId={}", param.getSessionId());
        return liveSessionDomainService.importSkus(param.getSessionId(), param.getSkuIds());
    }
}