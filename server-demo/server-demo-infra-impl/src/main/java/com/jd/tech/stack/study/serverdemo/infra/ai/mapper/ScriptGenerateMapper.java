package com.jd.tech.stack.study.serverdemo.infra.ai.mapper;

import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.ScriptGenerateDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Description: 脚本生成Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface ScriptGenerateMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO script_generate(session_id, content, aigc) VALUES(#{sessionId}, #{content}, #{aigc})")
    int insert(ScriptGenerateDO scriptGenerateDO);

    @Select("SELECT * FROM script_generate WHERE session_id = #{sessionId}")
    ScriptGenerateDO getBySessionId(Long sessionId);
}
