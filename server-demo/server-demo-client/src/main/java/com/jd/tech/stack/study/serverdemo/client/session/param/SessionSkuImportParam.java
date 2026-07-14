package com.jd.tech.stack.study.serverdemo.client.session.param;

import java.util.List;

/**
 * Description: 场次商品导入参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SessionSkuImportParam {

    /** 场次ID */
    private Long sessionId;

    /** 商品ID列表 */
    private List<String> skuIds;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<String> skuIds) {
        this.skuIds = skuIds;
    }
}