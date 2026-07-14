package com.jd.tech.stack.study.serverdemo.infra.price.mapper;

import com.jd.tech.stack.study.serverdemo.infra.price.dataobject.PriceCompareDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description: 价格对比Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface PriceCompareMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO price_compare(session_id, sku_id, sku_name, our_price, competitor_data, price_advantage, suggestion) VALUES(#{sessionId}, #{skuId}, #{skuName}, #{ourPrice}, #{competitorData}, #{priceAdvantage}, #{suggestion})")
    int insert(PriceCompareDO priceCompareDO);

    @Select("SELECT * FROM price_compare WHERE session_id = #{sessionId} AND sku_id = #{skuId}")
    PriceCompareDO getBySessionIdAndSkuId(Long sessionId, Long skuId);

    @Select("SELECT * FROM price_compare WHERE id = #{id}")
    PriceCompareDO getById(Long id);

    @Select("SELECT * FROM price_compare WHERE sku_id = #{skuId}")
    PriceCompareDO getBySkuId(String skuId);

    @Select("SELECT * FROM price_compare WHERE sku_id = #{skuId}")
    List<PriceCompareDO> listBySkuId(String skuId);
}
