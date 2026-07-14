package com.jd.tech.stack.study.serverdemo.domain.session.converter;

import com.jd.tech.stack.study.serverdemo.domain.session.bo.LiveSessionBO;
import com.jd.tech.stack.study.serverdemo.infra.session.dataobject.LiveSessionDO;

import java.util.Locale;

/**
 * Description: 直播场次DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class LiveSessionDOConverter {

    public static LiveSessionBO convert(LiveSessionDO sessionDO) {
        if (sessionDO == null) {
            return null;
        }
        LiveSessionBO bo = new LiveSessionBO();
        bo.setId(sessionDO.getId());
        bo.setSessionCode(sessionDO.getSessionCode());
        bo.setTitle(sessionDO.getTitle());
        bo.setStatus(parseStatus(sessionDO.getStatus()));
        bo.setStartTime(sessionDO.getPlannedStartTime());
        bo.setEndTime(sessionDO.getActualEndTime());
        bo.setOperator(sessionDO.getOperator());
        return bo;
    }

    public static LiveSessionDO convert(LiveSessionBO bo) {
        if (bo == null) {
            return null;
        }
        LiveSessionDO sessionDO = new LiveSessionDO();
        sessionDO.setId(bo.getId());
        sessionDO.setSessionCode(bo.getSessionCode());
        sessionDO.setTitle(bo.getTitle());
        sessionDO.setStatus(bo.getStatus() != null ? String.valueOf(bo.getStatus()) : null);
        sessionDO.setPlannedStartTime(bo.getStartTime());
        sessionDO.setActualEndTime(bo.getEndTime());
        sessionDO.setOperator(bo.getOperator());
        return sessionDO;
    }

    private static Integer parseStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(status);
        } catch (NumberFormatException ignored) {
            String normalized = status.trim().toUpperCase(Locale.ROOT);
            if ("CREATED".equals(normalized)) return 0;
            if ("READY".equals(normalized)) return 1;
            if ("LIVE".equals(normalized)) return 2;
            if ("FINISHED".equals(normalized)) return 3;
            if ("CLOSED".equals(normalized)) return 4;
            return 0;
        }
    }
}
