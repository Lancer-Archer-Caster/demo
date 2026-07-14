package com.jd.tech.stack.study.serverdemo.infra.price.gateway;

import com.jd.tech.stack.study.serverdemo.infra.price.dataobject.PriceCompareDO;

import java.util.List;

/**
 * Description: 价格对比网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface PriceCompareGateway {

    PriceCompareDO findBySessionIdAndSkuId(Long sessionId, Long skuId);

    PriceCompareDO findById(Long id);

    PriceCompareDO insert(PriceCompareDO priceCompareDO);

    PriceCompareDO findBySkuId(String skuId);

    List<PriceCompareDO> getCompetitorPrices(String skuId);

    String generateTalkpoint(String skuId);
}