package com.jd.tech.stack.study.serverdemo.domain.compliance.service;

import com.jd.tech.stack.study.serverdemo.domain.compliance.bo.ComplianceResultBO;
import com.jd.tech.stack.study.serverdemo.domain.compliance.bo.ComplianceHitBO;
import com.jd.tech.stack.study.serverdemo.domain.compliance.converter.ComplianceDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject.ComplianceResultDO;
import com.jd.tech.stack.study.serverdemo.infra.compliance.gateway.ComplianceGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 合规领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ComplianceDomainService {

    private static final Logger log = LoggerFactory.getLogger(ComplianceDomainService.class);

    @Autowired
    private ComplianceGateway complianceGateway;

    public ComplianceResultBO checkCompliance(String skuId) {
        return checkCompliance(skuId, null);
    }

    public ComplianceResultBO checkCompliance(String skuId, String content) {
        log.info("checkCompliance skuId={}", skuId);
        ComplianceResultDO complianceResultDO = complianceGateway.findBySkuId(skuId);
        ComplianceResultBO result = ComplianceDOConverter.convert(complianceResultDO);
        if (result == null) {
            result = new ComplianceResultBO();
        }
        List<ComplianceHitBO> hits = new ArrayList<>();
        String text = content == null ? "" : content;
        for (String word : complianceGateway.getComplianceWords()) {
            if (text.contains(word)) {
                ComplianceHitBO hit = new ComplianceHitBO();
                hit.setCategory("EXTREME_WORD");
                hit.setRuleName("本地极限词规则");
                hit.setHitContent(word);
                hit.setSuggestion("请将“" + word + "”替换为可验证的客观描述");
                hits.add(hit);
            }
        }
        result.setHits(hits);
        result.setHitCount(hits.size());
        result.setPassed(hits.isEmpty());
        return result;
    }

    public ComplianceResultBO getComplianceResult(Long id) {
        log.info("getComplianceResult id={}", id);
        ComplianceResultDO complianceResultDO = complianceGateway.findById(id);
        return ComplianceDOConverter.convert(complianceResultDO);
    }

    public List<String> getComplianceWords() {
        log.info("getComplianceWords");
        return complianceGateway.getComplianceWords();
    }
}
