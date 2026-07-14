package com.jd.tech.stack.study.serverdemo.client.sku.api;

import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.param.SkuQueryParam;

import java.util.List;

/**
 * Description: 商品服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface SkuService {

    /**
     * 查询商品详情
     */
    SkuDTO querySkuDetail(SkuQueryParam param);

    /**
     * 批量查询商品
     */
    List<SkuDTO> batchQuerySkus(List<String> skuIds);

    /**
     * 对比商品参数
     */
    SkuCompareDTO compareSkuParams(SkuQueryParam param);
}