package com.jd.tech.stack.study.serverdemo.client.sku.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 商品参数对比DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class SkuCompareDTO {

    /** 京东商品 */
    private SkuDTO jdSku;

    /** 竞品商品 */
    private SkuDTO competitorSku;

    /** 差异参数列表 */
    private List<ParamDiff> diffParams;
}