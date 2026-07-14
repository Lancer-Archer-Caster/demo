package com.jd.tech.stack.study.serverdemo.client.sku.param;

import lombok.Data;

/**
 * Description: 商品查询参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class SkuQueryParam {

    /** 商品ID */
    private String skuId;

    /** 场次ID */
    private Long sessionId;
}