package com.jd.tech.stack.study.serverdemo.app.danmaku.converter;

import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuAnswerDTO;
import com.jd.tech.stack.study.serverdemo.client.danmaku.dto.DanmakuDTO;
import com.jd.tech.stack.study.serverdemo.domain.danmaku.bo.DanmakuBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 弹幕BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class DanmakuBOConverter {

    public static DanmakuDTO convert(DanmakuBO bo) {
        if (bo == null) {
            return null;
        }
        DanmakuDTO dto = new DanmakuDTO();
        dto.setId(bo.getId());
        dto.setSessionId(bo.getSessionId());
        dto.setOriginalText(bo.getOriginalText());
        dto.setSenderNick(bo.getSenderNick());
        dto.setIntent(bo.getIntent());
        dto.setAiAnswer(bo.getAiAnswer());
        dto.setAiGenerated(bo.getAiGenerated());
        dto.setHumanConfirmed(bo.getHumanConfirmed());
        dto.setReplied(bo.getReplied());
        dto.setCreatedAt(bo.getCreatedAt());
        return dto;
    }

    public static DanmakuAnswerDTO convertAnswer(DanmakuBO bo) {
        if (bo == null) {
            return null;
        }
        DanmakuAnswerDTO dto = new DanmakuAnswerDTO();
        dto.setDanmakuId(bo.getId());
        dto.setAnswer(bo.getAiAnswer());
        dto.setAigc(bo.getAiGenerated());
        return dto;
    }

    public static List<DanmakuDTO> convertList(List<DanmakuBO> boList) {
        if (boList == null) {
            return null;
        }
        List<DanmakuDTO> dtoList = new ArrayList<>();
        for (DanmakuBO bo : boList) {
            dtoList.add(convert(bo));
        }
        return dtoList;
    }
}
