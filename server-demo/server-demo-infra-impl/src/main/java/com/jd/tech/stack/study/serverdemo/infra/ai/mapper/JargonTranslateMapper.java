package com.jd.tech.stack.study.serverdemo.infra.ai.mapper;

import com.jd.tech.stack.study.serverdemo.infra.ai.dataobject.JargonTranslateDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * Description: 术语翻译Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface JargonTranslateMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO jargon_translate(jargon, translation, category, aigc) VALUES(#{jargon}, #{translation}, #{category}, #{aigc})")
    int insert(JargonTranslateDO jargonTranslateDO);

    @Select("SELECT * FROM jargon_translate WHERE jargon = #{jargon}")
    JargonTranslateDO getByJargon(String jargon);
}
