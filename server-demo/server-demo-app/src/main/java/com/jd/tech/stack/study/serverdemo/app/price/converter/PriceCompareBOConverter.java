package com.jd.tech.stack.study.serverdemo.app.price.converter;

import com.jd.tech.stack.study.serverdemo.client.price.dto.CompetitorPrice;
import com.jd.tech.stack.study.serverdemo.client.price.dto.PriceCompareDTO;
import com.jd.tech.stack.study.serverdemo.domain.price.bo.CompetitorPriceBO;
import com.jd.tech.stack.study.serverdemo.domain.price.bo.PriceCompareBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 价格对比BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class PriceCompareBOConverter {

    public static PriceCompareDTO convert(PriceCompareBO bo) {
        if (bo == null) {
            return null;
        }
        PriceCompareDTO dto = new PriceCompareDTO();
        dto.setSkuId(bo.getSkuId() != null ? String.valueOf(bo.getSkuId()) : null);
        dto.setJdPrice(bo.getOurPrice());
        dto.setCompetitors(convertCompetitorList(bo.getCompetitorPrices()));
        dto.setPriceAdvantage(bo.getPriceAdvantage());
        return dto;
    }

    public static CompetitorPrice convertCompetitor(CompetitorPriceBO bo) {
        if (bo == null) {
            return null;
        }
        CompetitorPrice dto = new CompetitorPrice();
        dto.setPlatform(bo.getPlatform());
        dto.setPrice(bo.getPrice());
        dto.setProductName(bo.getProductName());
        return dto;
    }

    public static List<CompetitorPrice> convertCompetitorList(List<CompetitorPriceBO> boList) {
        if (boList == null) {
            return null;
        }
        List<CompetitorPrice> dtoList = new ArrayList<>();
        for (CompetitorPriceBO bo : boList) {
            dtoList.add(convertCompetitor(bo));
        }
        return dtoList;
    }
}