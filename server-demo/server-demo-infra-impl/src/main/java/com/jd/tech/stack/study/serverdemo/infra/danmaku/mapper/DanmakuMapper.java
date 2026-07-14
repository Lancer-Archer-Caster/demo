package com.jd.tech.stack.study.serverdemo.infra.danmaku.mapper;

import com.jd.tech.stack.study.serverdemo.infra.danmaku.dataobject.DanmakuDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Description: 弹幕Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface DanmakuMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO danmaku(session_id, content, sender_nick, intent, answer, aigc, human_confirmed, replied) VALUES(#{sessionId}, #{content}, #{senderNick}, #{intent}, #{answer}, #{aigc}, #{humanConfirmed}, #{replied})")
    int insert(DanmakuDO danmakuDO);

    @Select("SELECT * FROM danmaku WHERE id = #{id}")
    DanmakuDO getById(Long id);

    @Select("SELECT * FROM danmaku WHERE session_id = #{sessionId} ORDER BY id DESC LIMIT 100")
    List<DanmakuDO> getBySessionId(Long sessionId);

    @Update("UPDATE danmaku SET answer=#{answer}, intent=#{intent}, aigc=#{aigc}, human_confirmed=#{humanConfirmed}, replied=#{replied} WHERE id=#{id}")
    int update(DanmakuDO danmakuDO);
}
