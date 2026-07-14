package com.jd.tech.stack.study.serverdemo.client.price.dto;

import lombok.Data;

/**
 * Description: 竞品价格
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class CompetitorPrice {

    /** 平台 */
    private String platform;

    /** 价格 */
    private String price;

    /** 商品名称 */
    private String productName;
}