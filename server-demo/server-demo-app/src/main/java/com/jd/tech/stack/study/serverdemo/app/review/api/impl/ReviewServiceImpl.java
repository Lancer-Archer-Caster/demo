package com.jd.tech.stack.study.serverdemo.app.review.api.impl;

import com.jd.tech.stack.study.serverdemo.app.review.converter.ReviewBOConverter;
import com.jd.tech.stack.study.serverdemo.client.review.api.ReviewService;
import com.jd.tech.stack.study.serverdemo.client.review.dto.ReviewDTO;
import com.jd.tech.stack.study.serverdemo.client.review.param.ReviewQueryParam;
import com.jd.tech.stack.study.serverdemo.domain.review.service.ReviewDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 复盘服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    private ReviewDomainService reviewDomainService;

    @Override
    public ReviewDTO getSessionReview(ReviewQueryParam param) {
        log.info("getSessionReview sessionId={}", param.getSessionId());
        return ReviewBOConverter.convert(reviewDomainService.getSessionReview(param.getSessionId()));
    }

    @Override
    public Boolean backflowKnowledge(Long sessionId) {
        log.info("backflowKnowledge sessionId={}", sessionId);
        return reviewDomainService.backflowKnowledge(sessionId);
    }

    @Override
    public String exportReport(Long sessionId) {
        log.info("exportReport sessionId={}", sessionId);
        return reviewDomainService.exportReport(sessionId);
    }
}