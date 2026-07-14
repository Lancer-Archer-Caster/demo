package com.jd.tech.stack.study.serverdemo.infra.script.mapper;

import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptDO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptNodeDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Description: 脚本Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface ScriptMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO script(session_id, sku_id, title, content, ai_generated, status) VALUES(#{sessionId}, #{skuId}, #{title}, #{content}, #{aiGenerated}, #{status})")
    int insert(ScriptDO scriptDO);

    @Select("SELECT * FROM script WHERE id = #{id}")
    ScriptDO getById(Long id);

    @Select("SELECT * FROM script WHERE session_id = #{sessionId}")
    ScriptDO getBySessionId(Long sessionId);

    @Update("UPDATE script SET content=#{content}, status=#{status} WHERE id=#{id}")
    int update(ScriptDO scriptDO);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO script_node(script_id, node_index, node_type, content, keywords, sort_order, status) VALUES(#{scriptId}, #{nodeIndex}, #{nodeType}, #{content}, #{keywords}, #{sortOrder}, #{status})")
    int insertNode(ScriptNodeDO nodeDO);

    @Select("SELECT * FROM script_node WHERE script_id = #{scriptId} ORDER BY sort_order")
    List<ScriptNodeDO> getNodesByScriptId(Long scriptId);

    @Update("UPDATE script_node SET status=#{status} WHERE id=#{id}")
    int updateNodeStatus(@Param("id") Long id, @Param("status") String status);
}
