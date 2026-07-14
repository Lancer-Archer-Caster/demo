package com.jd.tech.stack.study.serverdemo.infra.compliance.mapper;

import com.jd.tech.stack.study.serverdemo.infra.compliance.dataobject.ComplianceResultDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Description: 合规检查结果Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface ComplianceResultMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO compliance_result(session_id, sku_id, sku_name, check_content, passed, hit_count) VALUES(#{sessionId}, #{skuId}, #{skuName}, #{checkContent}, #{passed}, #{hitCount})")
    int insert(ComplianceResultDO complianceResultDO);

    @Select("SELECT * FROM compliance_result WHERE session_id = #{sessionId} AND sku_id = #{skuId}")
    ComplianceResultDO getBySessionIdAndSkuId(Long sessionId, Long skuId);

    @Select("SELECT * FROM compliance_result WHERE id = #{id}")
    ComplianceResultDO getById(Long id);

    @Select("SELECT * FROM compliance_result WHERE sku_id = #{skuId}")
    ComplianceResultDO getBySkuId(String skuId);
}
