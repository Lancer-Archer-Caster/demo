package com.jd.tech.stack.study.serverdemo.domain.sku.service;

import com.jd.tech.stack.study.serverdemo.domain.sku.bo.ParamItemBO;
import com.jd.tech.stack.study.serverdemo.domain.sku.bo.SkuBO;
import com.jd.tech.stack.study.serverdemo.domain.sku.converter.SkuDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.sku.dataobject.SkuDO;
import com.jd.tech.stack.study.serverdemo.infra.sku.gateway.SkuGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: SKU领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class SkuDomainService {

    private static final Logger log = LoggerFactory.getLogger(SkuDomainService.class);

    @Autowired
    private SkuGateway skuGateway;

    public SkuBO querySkuDetail(String skuId) {
        log.info("querySkuDetail skuId={}", skuId);
        SkuDO skuDO = skuGateway.getBySkuId(skuId);
        return SkuDOConverter.convert(skuDO);
    }

    public List<SkuBO> batchQuerySkus(List<String> skuIds) {
        log.info("batchQuerySkus skuCount={}", skuIds != null ? skuIds.size() : 0);
        List<SkuDO> doList = skuGateway.listBySkuIds(skuIds);
        List<SkuBO> boList = new ArrayList<>();
        if (doList != null) {
            for (SkuDO skuDO : doList) {
                SkuBO bo = SkuDOConverter.convert(skuDO);
                if (bo != null) {
                    boList.add(bo);
                }
            }
        }
        return boList;
    }

    public Boolean compareSkuParams(String skuId1, String skuId2) {
        log.info("compareSkuParams skuId1={}, skuId2={}", skuId1, skuId2);
        SkuDO skuDO1 = skuGateway.getBySkuId(skuId1);
        SkuDO skuDO2 = skuGateway.getBySkuId(skuId2);
        SkuBO bo1 = SkuDOConverter.convert(skuDO1);
        SkuBO bo2 = SkuDOConverter.convert(skuDO2);
        if (bo1 == null || bo2 == null) {
            return false;
        }
        List<ParamItemBO> params1 = bo1.getParams();
        List<ParamItemBO> params2 = bo2.getParams();
        if (params1 == null || params2 == null) {
            return false;
        }
        return params1.size() == params2.size();
    }
}