package com.jd.tech.stack.study.serverdemo.domain.price.converter;

import com.jd.tech.stack.study.serverdemo.domain.price.bo.PriceCompareBO;
import com.jd.tech.stack.study.serverdemo.infra.price.dataobject.PriceCompareDO;

/**
 * Description: 价格对比DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class PriceCompareDOConverter {

    public static PriceCompareBO convert(PriceCompareDO priceCompareDO) {
        if (priceCompareDO == null) {
            return null;
        }
        PriceCompareBO bo = new PriceCompareBO();
        bo.setId(priceCompareDO.getId());
        bo.setSessionId(priceCompareDO.getSessionId());
        bo.setSkuId(priceCompareDO.getSkuId());
        bo.setSkuName(priceCompareDO.getSkuName());
        bo.setOurPrice(priceCompareDO.getOurPrice());
        bo.setPriceAdvantage(priceCompareDO.getPriceAdvantage());
        bo.setSuggestion(priceCompareDO.getSuggestion());
        bo.setGmtCreate(priceCompareDO.getGmtCreate());
        bo.setGmtModified(priceCompareDO.getGmtModified());
        return bo;
    }
}