package com.jd.tech.stack.study.serverdemo.client.ai.dto;

import lombok.Data;

/**
 * Description: 脚本生成DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ScriptGenerateDTO {

    /** 脚本内容 */
    private String content;

    /** 思考过程 */
    private String thinking;

    /** 是否AI生成 */
    private Boolean aigc;
}