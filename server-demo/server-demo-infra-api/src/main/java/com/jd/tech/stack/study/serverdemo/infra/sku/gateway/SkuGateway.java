package com.jd.tech.stack.study.serverdemo.infra.sku.gateway;

import com.jd.tech.stack.study.serverdemo.infra.sku.dataobject.SkuDO;

import java.util.List;

/**
 * Description: 商品网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface SkuGateway {

    SkuDO insert(SkuDO skuDO);

    SkuDO findById(Long id);

    List<SkuDO> findBySessionId(Long sessionId);

    SkuDO update(SkuDO skuDO);

    SkuDO getBySkuId(String skuId);

    List<SkuDO> listBySkuIds(List<String> skuIds);
}