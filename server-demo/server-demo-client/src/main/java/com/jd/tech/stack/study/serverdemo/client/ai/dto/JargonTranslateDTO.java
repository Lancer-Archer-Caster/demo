package com.jd.tech.stack.study.serverdemo.client.ai.dto;

import lombok.Data;

/**
 * Description: 术语翻译DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class JargonTranslateDTO {

    /** 术语 */
    private String term;

    /** 翻译 */
    private String translation;

    /** 分类 */
    private String category;

    /** 是否AI生成 */
    private Boolean aigc;
}