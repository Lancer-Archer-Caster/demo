package com.jd.tech.stack.study.serverdemo.app.danmaku.api.impl;

import com.jd.tech.stack.study.serverdemo.app.danmaku.converter.DanmakuBOConverter;
import com.jd.tech.stack.study.serverdemo.client.danmaku.api.DanmakuService;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuAnswerDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.param.DanmakuProcessParam;
import com.jd.tech.stack.study.serverdemo.domain.danmaku.bo.DanmakuBO;
import com.jd.tech.stack.study.serverdemo.domain.danmaku.service.DanmakuDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 弹幕服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class DanmakuServiceImpl implements DanmakuService {

    private static final Logger log = LoggerFactory.getLogger(DanmakuServiceImpl.class);

    @Autowired
    private DanmakuDomainService danmakuDomainService;

    @Override
    public DanmakuDTO processDanmaku(DanmakuProcessParam param) {
        log.info("processDanmaku sessionId={}, text={}", param.getSessionId(), param.getText());
        DanmakuBO bo = danmakuDomainService.processDanmaku(param.getSessionId(), param.getText(), param.getUserId());
        return DanmakuBOConverter.convert(bo);
    }

    @Override
    public DanmakuAnswerDTO generateAnswer(Long danmakuId) {
        log.info("generateAnswer danmakuId={}", danmakuId);
        DanmakuBO bo = danmakuDomainService.generateAnswer(danmakuId);
        return DanmakuBOConverter.convertAnswer(bo);
    }

    @Override
    public Boolean confirmAnswer(Long danmakuId, String answer) {
        log.info("confirmAnswer danmakuId={}", danmakuId);
        DanmakuBO bo = danmakuDomainService.confirmAnswer(danmakuId);
        return bo != null;
    }

    @Override
    public Boolean modifyAnswer(Long danmakuId, String answer) {
        log.info("modifyAnswer danmakuId={}", danmakuId);
        DanmakuBO bo = danmakuDomainService.modifyAnswer(danmakuId, answer);
        return bo != null;
    }

    @Override
    public List<DanmakuDTO> getDanmakuHistory(Long sessionId) {
        log.info("getDanmakuHistory sessionId={}", sessionId);
        List<DanmakuBO> boList = danmakuDomainService.getDanmakuHistory(sessionId);
        return DanmakuBOConverter.convertList(boList);
    }
}
