package com.jd.tech.stack.study.serverdemo.client.price.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 价格对比DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class PriceCompareDTO {

    /** 商品ID */
    private String skuId;

    /** 京东价格 */
    private String jdPrice;

    /** 竞品价格列表 */
    private List<CompetitorPrice> competitors;

    /** 价格优势描述 */
    private String priceAdvantage;
}