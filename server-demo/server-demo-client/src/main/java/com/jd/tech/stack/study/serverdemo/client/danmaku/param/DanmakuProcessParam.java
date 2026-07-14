package com.jd.tech.stack.study.serverdemo.client.danmaku.param;

import lombok.Data;

/**
 * Description: 弹幕处理参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class DanmakuProcessParam {

    /** 场次ID */
    private Long sessionId;

    /** 弹幕文本 */
    private String text;

    /** 用户ID */
    private String userId;
}