package com.jd.tech.stack.study.serverdemo.client.price.param;

import lombok.Data;

import java.util.List;

/**
 * Description: 价格对比参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class PriceCompareParam {

    /** 商品ID */
    private String skuId;

    /** 竞品平台列表 */
    private List<String> competitorPlatforms;
}