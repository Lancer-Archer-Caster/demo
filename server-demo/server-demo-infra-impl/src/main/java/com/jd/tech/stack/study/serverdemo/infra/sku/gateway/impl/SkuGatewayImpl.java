package com.jd.tech.stack.study.serverdemo.infra.sku.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.sku.dataobject.SkuDO;
import com.jd.tech.stack.study.serverdemo.infra.sku.gateway.SkuGateway;
import com.jd.tech.stack.study.serverdemo.infra.sku.mapper.SkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 商品网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class SkuGatewayImpl implements SkuGateway {

    @Autowired
    private SkuMapper skuMapper;

    @Override
    public SkuDO insert(SkuDO skuDO) {
        skuMapper.insert(skuDO);
        return skuDO;
    }

    @Override
    public SkuDO findById(Long id) {
        return skuMapper.getById(id);
    }

    @Override
    public List<SkuDO> findBySessionId(Long sessionId) {
        return skuMapper.getBySessionId(sessionId);
    }

    @Override
    public SkuDO update(SkuDO skuDO) {
        skuMapper.update(skuDO);
        return skuDO;
    }

    @Override
    public SkuDO getBySkuId(String skuId) {
        return skuMapper.getBySkuId(skuId);
    }

    @Override
    public List<SkuDO> listBySkuIds(List<String> skuIds) {
        return skuMapper.listBySkuIds(skuIds);
    }
}