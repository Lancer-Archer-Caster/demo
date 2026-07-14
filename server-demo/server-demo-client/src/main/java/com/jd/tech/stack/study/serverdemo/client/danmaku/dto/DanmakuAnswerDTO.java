package com.jd.tech.stack.study.serverdemo.client.danmaku.dto;

import lombok.Data;

/**
 * Description: 弹幕回答DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class DanmakuAnswerDTO {

    /** 弹幕ID */
    private Long danmakuId;

    /** 回答内容 */
    private String answer;

    /** 是否AI生成 */
    private Boolean aigc;

    /** 是否匹配知识库 */
    private Boolean knowledgeMatched;
}