package com.jd.tech.stack.study.serverdemo.app.compliance.api.impl;

import com.jd.tech.stack.study.serverdemo.app.compliance.converter.ComplianceBOConverter;
import com.jd.tech.stack.study.serverdemo.client.compliance.api.ComplianceService;
import com.jd.tech.stack.study.serverdemo.client.compliance.dto.ComplianceResultDTO;
import com.jd.tech.stack.study.serverdemo.client.compliance.param.ComplianceCheckParam;
import com.jd.tech.stack.study.serverdemo.domain.compliance.service.ComplianceDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 合规服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ComplianceServiceImpl implements ComplianceService {

    private static final Logger log = LoggerFactory.getLogger(ComplianceServiceImpl.class);

    @Autowired
    private ComplianceDomainService complianceDomainService;

    @Override
    public ComplianceResultDTO checkCompliance(ComplianceCheckParam param) {
        log.info("checkCompliance skuId={}", param.getSkuId());
        return ComplianceBOConverter.convert(complianceDomainService.checkCompliance(param.getSkuId(), param.getContent()));
    }

    @Override
    public List<String> getComplianceWords() {
        log.info("getComplianceWords");
        return complianceDomainService.getComplianceWords();
    }
}
