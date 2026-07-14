package com.jd.tech.stack.study.serverdemo.infra.session.mapper;

import com.jd.tech.stack.study.serverdemo.infra.session.dataobject.LiveSessionDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Description: 直播场次Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface LiveSessionMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO live_session(title, session_code, anchor_name, operator, status, planned_start_time, planned_end_time) VALUES(#{title}, #{sessionCode}, #{anchorName}, #{operator}, #{status}, #{plannedStartTime}, #{plannedEndTime})")
    int insert(LiveSessionDO sessionDO);

    @Select("SELECT * FROM live_session WHERE id = #{id}")
    LiveSessionDO getById(Long id);

    @Select("SELECT * FROM live_session WHERE status = #{status}")
    List<LiveSessionDO> getByStatus(String status);

    @Select("SELECT * FROM live_session")
    List<LiveSessionDO> getAll();

    @Update("UPDATE live_session SET status=#{status}, actual_start_time=#{actualStartTime}, actual_end_time=#{actualEndTime} WHERE id=#{id}")
    int update(LiveSessionDO sessionDO);
}
