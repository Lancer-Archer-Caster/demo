package com.jd.tech.stack.study.serverdemo.app.price.api.impl;

import com.jd.tech.stack.study.serverdemo.app.price.converter.PriceCompareBOConverter;
import com.jd.tech.stack.study.serverdemo.client.price.api.PriceCompareService;
import com.jd.tech.stack.study.serverdemo.client.price.dto.CompetitorPrice;
import com.jd.tech.stack.study.serverdemo.client.price.dto.PriceCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.price.param.PriceCompareParam;
import com.jd.tech.stack.study.serverdemo.domain.price.service.PriceCompareDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 价格对比服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class PriceCompareServiceImpl implements PriceCompareService {

    private static final Logger log = LoggerFactory.getLogger(PriceCompareServiceImpl.class);

    @Autowired
    private PriceCompareDomainService priceCompareDomainService;

    @Override
    public PriceCompareDTO comparePrice(PriceCompareParam param) {
        log.info("comparePrice skuId={}", param.getSkuId());
        return PriceCompareBOConverter.convert(priceCompareDomainService.comparePrice(param.getSkuId()));
    }

    @Override
    public List<CompetitorPrice> getCompetitorPrices(String skuId) {
        log.info("getCompetitorPrices skuId={}", skuId);
        // TODO: 获取竞品价格列表
        return PriceCompareBOConverter.convertCompetitorList(priceCompareDomainService.getCompetitorPrices(skuId));
    }

    @Override
    public String generateTalkpoint(String skuId) {
        log.info("generateTalkpoint skuId={}", skuId);
        return priceCompareDomainService.generateTalkpoint(skuId);
    }
}