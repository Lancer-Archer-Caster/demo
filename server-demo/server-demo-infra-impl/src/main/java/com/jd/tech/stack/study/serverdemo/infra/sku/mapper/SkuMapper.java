package com.jd.tech.stack.study.serverdemo.infra.sku.mapper;

import com.jd.tech.stack.study.serverdemo.infra.sku.dataobject.SkuDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Description: 商品Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface SkuMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO sku(session_id, sku_id, sku_name, main_image, category, brand, price, stock) VALUES(#{sessionId}, #{skuId}, #{skuName}, #{mainImage}, #{category}, #{brand}, #{price}, #{stock})")
    int insert(SkuDO skuDO);

    @Select("SELECT * FROM sku WHERE id = #{id}")
    SkuDO getById(Long id);

    @Select("SELECT * FROM sku WHERE session_id = #{sessionId}")
    List<SkuDO> getBySessionId(Long sessionId);

    @Select("SELECT * FROM sku WHERE sku_id = #{skuId}")
    SkuDO getBySkuId(String skuId);

    @Select({"<script>", "SELECT * FROM sku WHERE sku_id IN", "<foreach item='item' collection='skuIds' open='(' separator=',' close=')'>#{item}</foreach>", "</script>"})
    List<SkuDO> listBySkuIds(@Param("skuIds") List<String> skuIds);

    @Update("UPDATE sku SET price=#{price}, stock=#{stock} WHERE id=#{id}")
    int update(SkuDO skuDO);
}
