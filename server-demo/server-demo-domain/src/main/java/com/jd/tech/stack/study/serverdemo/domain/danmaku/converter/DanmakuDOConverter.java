package com.jd.tech.stack.study.serverdemo.domain.danmaku.converter;

import com.jd.tech.stack.study.serverdemo.domain.danmaku.bo.DanmakuBO;
import com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject.DanmakuDO;

/**
 * Description: 弹幕DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class DanmakuDOConverter {

    public static DanmakuBO convert(DanmakuDO danmakuDO) {
        if (danmakuDO == null) {
            return null;
        }
        DanmakuBO bo = new DanmakuBO();
        bo.setId(danmakuDO.getId());
        bo.setSessionId(danmakuDO.getSessionId());
        bo.setOriginalText(danmakuDO.getContent());
        bo.setSenderNick(danmakuDO.getSenderNick());
        bo.setIntent(danmakuDO.getIntent());
        bo.setAiAnswer(danmakuDO.getAnswer());
        bo.setAiGenerated(danmakuDO.getAigc());
        bo.setHumanConfirmed(danmakuDO.getHumanConfirmed());
        bo.setReplied(danmakuDO.getReplied());
        bo.setCreatedAt(danmakuDO.getGmtCreate());
        return bo;
    }

    public static DanmakuDO convert(DanmakuBO bo) {
        if (bo == null) {
            return null;
        }
        DanmakuDO danmakuDO = new DanmakuDO();
        danmakuDO.setId(bo.getId());
        danmakuDO.setSessionId(bo.getSessionId());
        danmakuDO.setContent(bo.getOriginalText());
        danmakuDO.setSenderNick(bo.getSenderNick());
        danmakuDO.setIntent(bo.getIntent());
        danmakuDO.setAnswer(bo.getAiAnswer());
        danmakuDO.setAigc(bo.getAiGenerated());
        danmakuDO.setHumanConfirmed(bo.getHumanConfirmed());
        danmakuDO.setReplied(bo.getReplied());
        danmakuDO.setGmtCreate(bo.getCreatedAt());
        return danmakuDO;
    }
}
