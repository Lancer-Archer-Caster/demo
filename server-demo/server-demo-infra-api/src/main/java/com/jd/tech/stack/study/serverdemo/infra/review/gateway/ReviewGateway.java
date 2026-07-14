package com.jd.tech.stack.study.serverdemo.infra.review.gateway;

import com.jd.tech.stack.study.serverdemo.infra.review.dataobject.ReviewDO;

import java.util.List;

/**
 * Description: 复盘网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ReviewGateway {

    ReviewDO insert(ReviewDO reviewDO);

    ReviewDO findById(Long id);

    ReviewDO findBySessionId(Long sessionId);

    List<ReviewDO> findAll();

    Boolean backflowKnowledge(Long sessionId);

    String exportReport(Long sessionId);
}