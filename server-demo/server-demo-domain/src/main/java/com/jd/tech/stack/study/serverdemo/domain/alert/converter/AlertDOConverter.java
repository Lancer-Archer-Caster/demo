package com.jd.tech.stack.study.serverdemo.domain.alert.converter;

import com.jd.tech.stack.study.serverdemo.domain.alert.bo.AlertBO;
import com.jd.tech.stack.study.serverdemo.infra.alert.dataobject.AlertDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 告警DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class AlertDOConverter {

    public static AlertBO convert(AlertDO alertDO) {
        if (alertDO == null) {
            return null;
        }
        AlertBO bo = new AlertBO();
        bo.setId(alertDO.getId());
        bo.setSessionId(alertDO.getSessionId());
        bo.setAlertType(alertDO.getAlertType());
        bo.setSkuId(alertDO.getSkuId());
        bo.setContent(alertDO.getMessage());
        bo.setLevel(alertDO.getLevel());
        bo.setCreatedAt(alertDO.getGmtCreate());
        return bo;
    }

    public static AlertDO convert(AlertBO bo) {
        if (bo == null) {
            return null;
        }
        AlertDO alertDO = new AlertDO();
        alertDO.setId(bo.getId());
        alertDO.setSessionId(bo.getSessionId());
        alertDO.setAlertType(bo.getAlertType());
        alertDO.setSkuId(bo.getSkuId());
        alertDO.setMessage(bo.getContent());
        alertDO.setLevel(bo.getLevel());
        alertDO.setGmtCreate(bo.getCreatedAt());
        return alertDO;
    }

    public static List<AlertBO> convertList(List<AlertDO> doList) {
        List<AlertBO> boList = new ArrayList<>();
        if (doList != null) {
            for (AlertDO alertDO : doList) {
                AlertBO bo = convert(alertDO);
                if (bo != null) {
                    boList.add(bo);
                }
            }
        }
        return boList;
    }
}