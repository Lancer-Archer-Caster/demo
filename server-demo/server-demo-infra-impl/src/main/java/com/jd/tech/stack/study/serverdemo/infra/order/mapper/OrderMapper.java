package com.jd.tech.stack.study.serverdemo.infra.order.mapper;

import com.jd.tech.stack.study.serverdemo.infra.order.dataobject.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Description: Demo
 *
 * @author DongBoot
 * @see <a href="https://mybatis.org/spring-boot-starter/">Mybatis 使用说明</a>
 */
@Mapper
public interface OrderMapper {

    @Select("SELECT * FROM `order` WHERE id = #{id}")
    OrderDO getById(String id);

}
