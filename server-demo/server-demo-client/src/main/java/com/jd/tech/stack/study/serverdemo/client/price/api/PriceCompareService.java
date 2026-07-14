package com.jd.tech.stack.study.serverdemo.client.price.api;

import com.jd.tech.stack.study.serverdemo.client.price.dto.PriceCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.price.dto.CompetitorPrice;
import com.jd.tech.stack.study.serverdemo.client.price.param.PriceCompareParam;

import java.util.List;

/**
 * Description: 价格对比服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface PriceCompareService {

    /**
     * 价格对比
     */
    PriceCompareDTO comparePrice(PriceCompareParam param);

    /**
     * 获取竞品价格
     */
    List<CompetitorPrice> getCompetitorPrices(String skuId);

    /**
     * 生成话术要点
     */
    String generateTalkpoint(String skuId);
}