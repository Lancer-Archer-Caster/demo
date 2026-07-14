package com.jd.tech.stack.study.serverdemo.domain.compliance.converter;

import com.jd.tech.stack.study.serverdemo.domain.compliance.bo.ComplianceResultBO;
import com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject.ComplianceResultDO;

/**
 * Description: 合规DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ComplianceDOConverter {

    public static ComplianceResultBO convert(ComplianceResultDO complianceResultDO) {
        if (complianceResultDO == null) {
            return null;
        }
        ComplianceResultBO bo = new ComplianceResultBO();
        bo.setId(complianceResultDO.getId());
        bo.setSessionId(complianceResultDO.getSessionId());
        bo.setSkuId(complianceResultDO.getSkuId());
        bo.setSkuName(complianceResultDO.getSkuName());
        bo.setCheckContent(complianceResultDO.getCheckContent());
        bo.setPassed(complianceResultDO.getPassed());
        bo.setHitCount(complianceResultDO.getHitCount());
        bo.setGmtCreate(complianceResultDO.getGmtCreate());
        bo.setGmtModified(complianceResultDO.getGmtModified());
        return bo;
    }
}