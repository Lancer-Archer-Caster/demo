package com.jd.tech.stack.study.serverdemo.infra.price.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.price.dataobject.PriceCompareDO;
import com.jd.tech.stack.study.serverdemo.infra.price.gateway.PriceCompareGateway;
import com.jd.tech.stack.study.serverdemo.infra.price.mapper.PriceCompareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 价格对比网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class PriceCompareGatewayImpl implements PriceCompareGateway {

    @Autowired
    private PriceCompareMapper priceCompareMapper;

    @Override
    public PriceCompareDO findBySessionIdAndSkuId(Long sessionId, Long skuId) {
        return priceCompareMapper.getBySessionIdAndSkuId(sessionId, skuId);
    }

    @Override
    public PriceCompareDO findById(Long id) {
        return priceCompareMapper.getById(id);
    }

    @Override
    public PriceCompareDO insert(PriceCompareDO priceCompareDO) {
        priceCompareMapper.insert(priceCompareDO);
        return priceCompareDO;
    }

    @Override
    public PriceCompareDO findBySkuId(String skuId) {
        return priceCompareMapper.getBySkuId(skuId);
    }

    @Override
    public List<PriceCompareDO> getCompetitorPrices(String skuId) {
        // TODO: 调用竞品价格API获取数据
        return priceCompareMapper.listBySkuId(skuId);
    }

    @Override
    public String generateTalkpoint(String skuId) {
        return "本地比价显示本店样例价更低，请强调可验证的价格与服务信息。";
    }
}
