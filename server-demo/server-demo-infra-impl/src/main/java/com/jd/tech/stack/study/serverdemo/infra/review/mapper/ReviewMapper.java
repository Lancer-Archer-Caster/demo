package com.jd.tech.stack.study.serverdemo.infra.review.mapper;

import com.jd.tech.stack.study.serverdemo.infra.review.dataobject.ReviewDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description: 复盘Mapper
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Mapper
public interface ReviewMapper {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO review(session_id, title, summary, metrics_data, highlights, improvements) VALUES(#{sessionId}, #{title}, #{summary}, #{metricsData}, #{highlights}, #{improvements})")
    int insert(ReviewDO reviewDO);

    @Select("SELECT * FROM review WHERE id = #{id}")
    ReviewDO getById(Long id);

    @Select("SELECT * FROM review WHERE session_id = #{sessionId}")
    ReviewDO getBySessionId(Long sessionId);

    @Select("SELECT * FROM review")
    List<ReviewDO> getAll();
}
