package com.jd.tech.stack.study.serverdemo.infra.item.mapper;

import com.jd.tech.stack.study.serverdemo.infra.item.dataobject.ItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Description: Demo
 *
 * @author DongBoot
 * @see <a href="https://mybatis.org/spring-boot-starter/">Mybatis 使用说明</a>
 */
@Mapper
public interface ItemMapper {
    @Select("SELECT * FROM item WHERE sku_id = #{id}")
    ItemDO getById(String id);
}
