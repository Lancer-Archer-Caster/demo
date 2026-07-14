package com.jd.tech.stack.study.serverdemo.domain.price.service;

import com.jd.tech.stack.study.serverdemo.domain.price.bo.CompetitorPriceBO;
import com.jd.tech.stack.study.serverdemo.domain.price.bo.PriceCompareBO;
import com.jd.tech.stack.study.serverdemo.domain.price.converter.PriceCompareDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.price.dataobject.PriceCompareDO;
import com.jd.tech.stack.study.serverdemo.infra.price.gateway.PriceCompareGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 价格对比领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class PriceCompareDomainService {

    private static final Logger log = LoggerFactory.getLogger(PriceCompareDomainService.class);

    @Autowired
    private PriceCompareGateway priceCompareGateway;

    public PriceCompareBO comparePrice(String skuId) {
        log.info("comparePrice skuId={}", skuId);
        // TODO: 价格对比算法，目前仅查询已有记录
        PriceCompareDO priceCompareDO = priceCompareGateway.findBySkuId(skuId);
        return PriceCompareDOConverter.convert(priceCompareDO);
    }

    public PriceCompareBO getPriceCompare(Long id) {
        log.info("getPriceCompare id={}", id);
        PriceCompareDO priceCompareDO = priceCompareGateway.findById(id);
        return PriceCompareDOConverter.convert(priceCompareDO);
    }

    public List<CompetitorPriceBO> getCompetitorPrices(String skuId) {
        log.info("getCompetitorPrices skuId={}", skuId);
        List<CompetitorPriceBO> prices = new ArrayList<>();
        prices.add(competitor("本地平台A", "3149", "同档智能手机 256GB"));
        prices.add(competitor("本地平台B", "3099", "同档智能手机 标准版"));
        prices.add(competitor("本店自营", "2999", "5G智能手机 骁龙8 Gen3"));
        return prices;
    }

    public String generateTalkpoint(String skuId) {
        log.info("generateTalkpoint skuId={}", skuId);
        return priceCompareGateway.generateTalkpoint(skuId);
    }

    private CompetitorPriceBO competitor(String platform, String price, String productName) {
        CompetitorPriceBO competitor = new CompetitorPriceBO();
        competitor.setPlatform(platform);
        competitor.setPrice(price);
        competitor.setProductName(productName);
        return competitor;
    }
}
