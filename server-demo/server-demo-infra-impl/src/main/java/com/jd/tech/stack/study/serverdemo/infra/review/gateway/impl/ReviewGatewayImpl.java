package com.jd.tech.stack.study.serverdemo.infra.review.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.review.dataobject.ReviewDO;
import com.jd.tech.stack.study.serverdemo.infra.review.gateway.ReviewGateway;
import com.jd.tech.stack.study.serverdemo.infra.review.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 复盘网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ReviewGatewayImpl implements ReviewGateway {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public ReviewDO insert(ReviewDO reviewDO) {
        reviewMapper.insert(reviewDO);
        return reviewDO;
    }

    @Override
    public ReviewDO findById(Long id) {
        return reviewMapper.getById(id);
    }

    @Override
    public ReviewDO findBySessionId(Long sessionId) {
        return reviewMapper.getBySessionId(sessionId);
    }

    @Override
    public List<ReviewDO> findAll() {
        return reviewMapper.getAll();
    }

    @Override
    public Boolean backflowKnowledge(Long sessionId) {
        // TODO: 实现知识回流逻辑
        return true;
    }

    @Override
    public String exportReport(Long sessionId) {
        // TODO: 实现报告导出逻辑
        ReviewDO reviewDO = reviewMapper.getBySessionId(sessionId);
        return reviewDO != null ? reviewDO.getSummary() : "";
    }
}