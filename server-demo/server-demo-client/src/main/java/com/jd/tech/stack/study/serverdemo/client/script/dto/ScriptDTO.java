package com.jd.tech.stack.study.serverdemo.client.script.dto;

import lombok.Data;

import java.util.List;

/**
 * Description: 脚本DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ScriptDTO {

    /** 脚本ID */
    private Long id;

    /** 场次ID */
    private Long sessionId;

    /** 商品ID */
    private String skuId;

    /** 脚本内容 */
    private String content;

    /** 是否AI生成 */
    private Boolean aiGenerated;

    /** 脚本节点列表 */
    private List<ScriptNodeDTO> nodes;
}