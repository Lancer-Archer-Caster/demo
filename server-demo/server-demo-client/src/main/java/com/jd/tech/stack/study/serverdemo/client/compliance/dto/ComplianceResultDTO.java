package com.jd.tech.stack.study.serverdemo.client.compliance.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 合规检查结果DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ComplianceResultDTO {

    /** 是否通过 */
    private Boolean passed;

    /** 命中列表 */
    private List<ComplianceHit> hits;

    /** 建议列表 */
    private List<String> suggestions;
}