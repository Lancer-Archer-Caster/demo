package com.jd.tech.stack.study.serverdemo.constants;

/**
 * Description: 直播业务常量
 *
 * @author DongBoot
 */
public class LiveConstants {

    private LiveConstants() {
    }

    // ==================== 降级阈值 ====================

    /**
     * ASR超时阈值2秒
     */
    public static final int ASR_TIMEOUT_MS = 2000;

    /**
     * AI大模型超时阈值30秒
     */
    public static final int AI_TIMEOUT_MS = 30000;

    /**
     * 比价接口超时阈值5秒
     */
    public static final int PRICE_TIMEOUT_MS = 5000;

    // ==================== 库存告警阈值 ====================

    /**
     * 库存低于10%告警
     */
    public static final double INVENTORY_ALERT_THRESHOLD = 0.1;

    // ==================== AIGC标识 ====================

    /**
     * AIGC内容水印标识
     */
    public static final String AIGC_WATERMARK = "[由京东大模型生成]";

    // ==================== 弹幕过滤 ====================

    /**
     * 弹幕最大长度
     */
    public static final int DANMAKU_MAX_LENGTH = 500;

    /**
     * RAG检索返回Top3
     */
    public static final int RAG_TOP_K = 3;

    // ==================== WebSocket频道 ====================

    /**
     * 告警推送频道
     */
    public static final String WS_CHANNEL_ALERT = "/ws/alert";

    /**
     * ASR识别推送频道
     */
    public static final String WS_CHANNEL_ASR = "/ws/asr";

    /**
     * 弹幕推送频道
     */
    public static final String WS_CHANNEL_DANMAKU = "/ws/danmaku";

    /**
     * 价格推送频道
     */
    public static final String WS_CHANNEL_PRICE = "/ws/price";

}