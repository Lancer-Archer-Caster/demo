package com.jd.tech.stack.study.serverdemo.app.session.converter;

import com.jd.tech.stack.study.serverdemo.client.session.dto.LiveSessionDTO;
import com.jd.tech.stack.study.serverdemo.domain.session.bo.LiveSessionBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 场次BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class LiveSessionBOConverter {

    public static LiveSessionDTO convert(LiveSessionBO bo) {
        if (bo == null) {
            return null;
        }
        LiveSessionDTO dto = new LiveSessionDTO();
        dto.setId(bo.getId());
        dto.setSessionCode(bo.getSessionCode());
        dto.setTitle(bo.getTitle());
        dto.setStatus(bo.getStatus());
        dto.setStartTime(bo.getStartTime());
        dto.setEndTime(bo.getEndTime());
        dto.setOperator(bo.getOperator());
        return dto;
    }

    public static List<LiveSessionDTO> convertList(List<LiveSessionBO> boList) {
        if (boList == null) {
            return null;
        }
        List<LiveSessionDTO> dtoList = new ArrayList<>();
        for (LiveSessionBO bo : boList) {
            dtoList.add(convert(bo));
        }
        return dtoList;
    }
}