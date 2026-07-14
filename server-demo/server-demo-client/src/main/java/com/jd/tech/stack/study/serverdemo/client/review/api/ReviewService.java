package com.jd.tech.stack.study.serverdemo.client.review.api;

import com.jd.tech.stack.study.serverdemo.client.review.dto.ReviewDTO;
import com.jd.tech.stack.study.serverdemo.client.review.param.ReviewQueryParam;

/**
 * Description: 复盘服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ReviewService {

    /**
     * 获取场次复盘
     */
    ReviewDTO getSessionReview(ReviewQueryParam param);

    /**
     * 知识回流
     */
    Boolean backflowKnowledge(Long sessionId);

    /**
     * 导出报告
     */
    String exportReport(Long sessionId);
}