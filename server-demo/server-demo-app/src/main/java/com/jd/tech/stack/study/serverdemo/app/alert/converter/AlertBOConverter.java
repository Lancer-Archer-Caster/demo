package com.jd.tech.stack.study.serverdemo.app.alert.converter;

import com.jd.tech.stack.study.serverdemo.client.alert.dto.AlertDTO;
import com.jd.tech.stack.study.serverdemo.domain.alert.bo.AlertBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 告警BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class AlertBOConverter {

    public static AlertDTO convert(AlertBO bo) {
        if (bo == null) {
            return null;
        }
        AlertDTO dto = new AlertDTO();
        dto.setId(bo.getId());
        dto.setSessionId(bo.getSessionId());
        dto.setAlertType(bo.getAlertType());
        dto.setSkuId(bo.getSkuId());
        dto.setContent(bo.getContent());
        dto.setLevel(bo.getLevel());
        dto.setCreatedAt(bo.getCreatedAt());
        return dto;
    }

    public static List<AlertDTO> convertList(List<AlertBO> boList) {
        if (boList == null) {
            return null;
        }
        List<AlertDTO> dtoList = new ArrayList<>();
        for (AlertBO bo : boList) {
            dtoList.add(convert(bo));
        }
        return dtoList;
    }
}