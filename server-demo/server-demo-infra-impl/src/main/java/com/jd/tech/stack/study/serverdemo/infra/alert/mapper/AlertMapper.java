package com.jd.tech.stack.study.serverdemo.infra.alert.mapper;

import com.jd.tech.stack.study.serverdemo.infra.alert.dataobject.AlertDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Description: 告警Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface AlertMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO alert(session_id, alert_type, level, sku_id, message) VALUES(#{sessionId}, #{alertType}, #{level}, #{skuId}, #{message})")
    int insert(AlertDO alertDO);

    @Select("SELECT * FROM alert WHERE id = #{id}")
    AlertDO getById(Long id);

    @Select("SELECT * FROM alert WHERE session_id = #{sessionId}")
    List<AlertDO> getBySessionId(Long sessionId);

    @Select("SELECT * FROM alert WHERE session_id = #{sessionId} AND handled = '0'")
    List<AlertDO> getActiveBySessionId(Long sessionId);

    @Select("SELECT * FROM alert WHERE session_id = #{sessionId} AND handled = '1'")
    List<AlertDO> getHistoryBySessionId(Long sessionId);

    @Update("UPDATE alert SET handled=#{handled}, handler=#{handler} WHERE id=#{id}")
    int update(AlertDO alertDO);
}
