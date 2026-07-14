package com.jd.tech.stack.study.serverdemo.infra.compliance.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject.ComplianceResultDO;
import com.jd.tech.stack.study.serverdemo.infra.compliance.gateway.ComplianceGateway;
import com.jd.tech.stack.study.serverdemo.infra.compliance.mapper.ComplianceResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 合规网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ComplianceGatewayImpl implements ComplianceGateway {

    @Autowired
    private ComplianceResultMapper complianceResultMapper;

    @Override
    public ComplianceResultDO findBySessionIdAndSkuId(Long sessionId, Long skuId) {
        return complianceResultMapper.getBySessionIdAndSkuId(sessionId, skuId);
    }

    @Override
    public ComplianceResultDO findById(Long id) {
        return complianceResultMapper.getById(id);
    }

    @Override
    public ComplianceResultDO insert(ComplianceResultDO complianceResultDO) {
        complianceResultMapper.insert(complianceResultDO);
        return complianceResultDO;
    }

    @Override
    public ComplianceResultDO findBySkuId(String skuId) {
        return complianceResultMapper.getBySkuId(skuId);
    }

    @Override
    public List<String> getComplianceWords() {
        return Arrays.asList("最", "第一", "顶级", "极品", "绝无仅有", "100%", "国家级", "世界级");
    }
}
