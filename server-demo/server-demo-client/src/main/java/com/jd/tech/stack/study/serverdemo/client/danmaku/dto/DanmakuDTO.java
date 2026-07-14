package com.jd.tech.stack.study.serverdemo.client.danmaku.dto;

import lombok.Data;

/**
 * Description: 弹幕DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class DanmakuDTO {

    /** 弹幕ID */
    private Long id;

    /** 场次ID */
    private Long sessionId;

    /** 原始文本 */
    private String originalText;

    /** 评论用户昵称/平台用户标识 */
    private String senderNick;

    /** 意图分类 */
    private String intent;

    /** AI回答 */
    private String aiAnswer;

    /** 是否AI生成 */
    private Boolean aiGenerated;

    /** 是否人工确认 */
    private Boolean humanConfirmed;

    /** 是否已回复 */
    private Boolean replied;

    /** 评论接收时间 */
    private String createdAt;
}
