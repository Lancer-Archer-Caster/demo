package com.jd.tech.stack.study.serverdemo.domain.sku.converter;

import com.jd.tech.stack.study.serverdemo.domain.sku.bo.SkuBO;
import com.jd.tech.stack.study.serverdemo.infra.sku.dataobject.SkuDO;

import java.util.ArrayList;

/**
 * Description: SKU DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SkuDOConverter {

    public static SkuBO convert(SkuDO skuDO) {
        if (skuDO == null) {
            return null;
        }
        SkuBO bo = new SkuBO();
        bo.setSkuId(skuDO.getSkuId());
        bo.setSkuName(skuDO.getSkuName());
        bo.setMainImage(skuDO.getMainImage());
        bo.setPrice(skuDO.getPrice());
        // params not stored in DO, set as empty list
        bo.setParams(new ArrayList<>());
        return bo;
    }

    public static SkuDO convert(SkuBO bo) {
        if (bo == null) {
            return null;
        }
        SkuDO skuDO = new SkuDO();
        skuDO.setSkuId(bo.getSkuId());
        skuDO.setSkuName(bo.getSkuName());
        skuDO.setMainImage(bo.getMainImage());
        skuDO.setPrice(bo.getPrice());
        // params not stored in DO, skip
        return skuDO;
    }

}