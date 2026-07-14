package com.jd.tech.stack.study.serverdemo.client.compliance.api;

import com.jd.tech.stack.study.serverdemo.client.compliance.dto.ComplianceResultDTO;
import com.jd.tech.stack.study.serverdemo.client.compliance.param.ComplianceCheckParam;

import java.util.List;

/**
 * Description: 合规服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ComplianceService {

    /**
     * 合规检查
     */
    ComplianceResultDTO checkCompliance(ComplianceCheckParam param);

    /**
     * 获取合规词库
     */
    List<String> getComplianceWords();
}