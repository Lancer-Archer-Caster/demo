package com.jd.tech.stack.study.serverdemo.client.sku.dto;

import lombok.Data;

/**
 * Description: 参数差异项
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ParamDiff {

    /** 参数名 */
    private String paramName;

    /** 京东值 */
    private String jdValue;

    /** 竞品值 */
    private String competitorValue;
}