package com.jd.tech.stack.study.serverdemo.client.compliance.dto;

import lombok.Data;

/**
 * Description: 合规命中项
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ComplianceHit {

    /** 命中词 */
    private String word;

    /** 分类 */
    private String category;

    /** 位置 */
    private Integer position;
}