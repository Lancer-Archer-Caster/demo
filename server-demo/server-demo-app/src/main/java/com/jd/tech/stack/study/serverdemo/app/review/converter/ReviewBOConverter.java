package com.jd.tech.stack.study.serverdemo.app.review.converter;

import com.jd.tech.stack.study.serverdemo.client.review.dto.ReviewDTO;
import com.jd.tech.stack.study.serverdemo.domain.review.bo.ReviewBO;

/**
 * Description: 复盘BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ReviewBOConverter {

    public static ReviewDTO convert(ReviewBO bo) {
        if (bo == null) {
            return null;
        }
        ReviewDTO dto = new ReviewDTO();
        dto.setSessionId(bo.getSessionId());
        dto.setSessionName(bo.getSessionName());
        dto.setDate(bo.getDate());
        dto.setMetrics(bo.getMetrics());
        dto.setAlertSummary(bo.getAlertSummary());
        dto.setDanmakuSummary(bo.getDanmakuSummary());
        dto.setKnowledgeItems(bo.getKnowledgeItems());
        dto.setTimeline(bo.getTimeline());
        return dto;
    }
}