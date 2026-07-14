package com.jd.tech.stack.study.serverdemo.domain.review.converter;

import com.jd.tech.stack.study.serverdemo.domain.review.bo.ReviewBO;
import com.jd.tech.stack.study.serverdemo.infra.review.dataobject.ReviewDO;

/**
 * Description: 复盘DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ReviewDOConverter {

    public static ReviewBO convert(ReviewDO reviewDO) {
        if (reviewDO == null) {
            return null;
        }
        ReviewBO bo = new ReviewBO();
        bo.setSessionId(reviewDO.getSessionId());
        bo.setSessionName(reviewDO.getTitle());
        bo.setDate(reviewDO.getGmtCreate());
        // metricsData is JSON string, needs parsing - TODO
        bo.setMetrics(null);
        bo.setAlertSummary(reviewDO.getHighlights());
        bo.setDanmakuSummary(reviewDO.getImprovements());
        // knowledgeItems and timeline not stored in DO
        bo.setKnowledgeItems(null);
        bo.setTimeline(null);
        return bo;
    }
}