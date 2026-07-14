package com.jd.tech.stack.study.serverdemo.infra.compliance.gateway;

import com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject.ComplianceResultDO;

import java.util.List;

/**
 * Description: 合规网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ComplianceGateway {

    ComplianceResultDO findBySessionIdAndSkuId(Long sessionId, Long skuId);

    ComplianceResultDO findById(Long id);

    ComplianceResultDO insert(ComplianceResultDO complianceResultDO);

    ComplianceResultDO findBySkuId(String skuId);

    List<String> getComplianceWords();
}