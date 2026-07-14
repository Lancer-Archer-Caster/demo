package com.jd.tech.stack.study.serverdemo.domain.danmaku.service;

import com.jd.tech.stack.study.serverdemo.domain.danmaku.bo.DanmakuBO;
import com.jd.tech.stack.study.serverdemo.domain.danmaku.converter.DanmakuDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject.DanmakuDO;
import com.jd.tech.stack.study.serverdemo.infra.danmaku.gateway.DanmakuGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹幕领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class DanmakuDomainService {

    private static final Logger log = LoggerFactory.getLogger(DanmakuDomainService.class);

    @Autowired
    private DanmakuGateway danmakuGateway;

    public DanmakuBO processDanmaku(Long sessionId, String text) {
        return processDanmaku(sessionId, text, "本地观众");
    }

    public DanmakuBO processDanmaku(Long sessionId, String text, String senderNick) {
        log.info("processDanmaku sessionId={}, text={}", sessionId, text);
        DanmakuDO danmakuDO = new DanmakuDO();
        danmakuDO.setSessionId(sessionId);
        danmakuDO.setContent(text);
        danmakuDO.setSenderNick(senderNick == null || senderNick.trim().isEmpty() ? "匿名观众" : senderNick);
        boolean water = isWaterComment(text);
        danmakuDO.setIntent(water ? "WATER" : "QA");
        danmakuDO.setAnswer(water ? null : localAnswer(text));
        danmakuDO.setAigc(false);
        danmakuDO.setHumanConfirmed(false);
        danmakuDO.setReplied(false);
        danmakuDO = danmakuGateway.insert(danmakuDO);
        return DanmakuDOConverter.convert(danmakuDO);
    }

    public DanmakuBO generateAnswer(Long danmakuId) {
        log.info("generateAnswer danmakuId={}", danmakuId);
        DanmakuDO danmakuDO = danmakuGateway.generateAnswer(danmakuId);
        return DanmakuDOConverter.convert(danmakuDO);
    }

    public DanmakuBO confirmAnswer(Long danmakuId) {
        log.info("confirmAnswer danmakuId={}", danmakuId);
        DanmakuDO danmakuDO = danmakuGateway.findById(danmakuId);
        if (danmakuDO != null) {
            danmakuDO.setHumanConfirmed(true);
            danmakuGateway.update(danmakuDO);
        }
        return DanmakuDOConverter.convert(danmakuDO);
    }

    public DanmakuBO modifyAnswer(Long danmakuId, String newAnswer) {
        log.info("modifyAnswer danmakuId={}", danmakuId);
        DanmakuDO danmakuDO = danmakuGateway.findById(danmakuId);
        if (danmakuDO != null) {
            danmakuDO.setAnswer(newAnswer);
            danmakuGateway.update(danmakuDO);
        }
        return DanmakuDOConverter.convert(danmakuDO);
    }

    public List<DanmakuBO> getDanmakuHistory(Long sessionId) {
        log.info("getDanmakuHistory sessionId={}", sessionId);
        List<DanmakuDO> doList = danmakuGateway.findBySessionId(sessionId);
        List<DanmakuBO> boList = new ArrayList<>();
        if (doList != null) {
            for (DanmakuDO danmakuDO : doList) {
                DanmakuBO bo = DanmakuDOConverter.convert(danmakuDO);
                if (bo != null) {
                    boList.add(bo);
                }
            }
        }
        return boList;
    }

    private boolean isWaterComment(String text) {
        if (text == null) return true;
        String normalized = text.trim().toLowerCase();
        return normalized.length() <= 3
                || "666".equals(normalized)
                || normalized.contains("来了来了")
                || normalized.contains("哈哈哈");
    }

    private String localAnswer(String text) {
        String value = text == null ? "" : text;
        if (value.contains("快充") || value.contains("充电")) {
            return "支持 67W 快充，具体能力以商品详情页为准。";
        }
        if (value.contains("刷新率") || value.contains("屏幕")) {
            return "本地知识库记录为 120Hz OLED 屏幕，请以实际商品参数为准。";
        }
        if (value.toUpperCase().contains("NFC")) {
            return "本地知识库显示支持 NFC，建议主播结合商品详情确认。";
        }
        if (value.contains("续航") || value.contains("电池")) {
            return "5000mAh 电池的实际续航因使用场景而异，建议避免绝对化承诺。";
        }
        return null;
    }
}
