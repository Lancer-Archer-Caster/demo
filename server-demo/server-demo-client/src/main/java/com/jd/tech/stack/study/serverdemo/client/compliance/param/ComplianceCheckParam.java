package com.jd.tech.stack.study.serverdemo.client.compliance.param;

import lombok.Data;

/**
 * Description: 合规检查参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ComplianceCheckParam {

    /** 待检查内容 */
    private String content;

    /** 商品ID */
    private String skuId;
}