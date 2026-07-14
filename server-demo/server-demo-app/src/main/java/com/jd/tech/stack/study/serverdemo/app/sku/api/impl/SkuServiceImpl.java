package com.jd.tech.stack.study.serverdemo.app.sku.api.impl;

import com.jd.tech.stack.study.serverdemo.app.sku.converter.SkuBOConverter;
import com.jd.tech.stack.study.serverdemo.client.sku.api.SkuService;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.param.SkuQueryParam;
import com.jd.tech.stack.study.serverdemo.domain.sku.bo.SkuBO;
import com.jd.tech.stack.study.serverdemo.domain.sku.service.SkuDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 商品服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class SkuServiceImpl implements SkuService {

    private static final Logger log = LoggerFactory.getLogger(SkuServiceImpl.class);

    @Autowired
    private SkuDomainService skuDomainService;

    @Override
    public SkuDTO querySkuDetail(SkuQueryParam param) {
        log.info("querySkuDetail skuId={}", param.getSkuId());
        SkuBO bo = skuDomainService.querySkuDetail(param.getSkuId());
        return SkuBOConverter.convert(bo);
    }

    @Override
    public List<SkuDTO> batchQuerySkus(List<String> skuIds) {
        log.info("batchQuerySkus skuCount={}", skuIds != null ? skuIds.size() : 0);
        List<SkuBO> boList = skuDomainService.batchQuerySkus(skuIds);
        return SkuBOConverter.convertList(boList);
    }

    @Override
    public SkuCompareDTO compareSkuParams(SkuQueryParam param) {
        log.info("compareSkuParams skuId={}", param.getSkuId());
        // TODO: 实现商品参数对比逻辑
        return new SkuCompareDTO();
    }
}