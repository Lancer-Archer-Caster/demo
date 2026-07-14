package com.jd.tech.stack.study.serverdemo.client.script.dto;

import lombok.Data;

/**
 * Description: 脚本节点DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ScriptNodeDTO {

    /** 节点ID */
    private Long id;

    /** 节点序号 */
    private Integer nodeIndex;

    /** 节点内容 */
    private String content;

    /** 关键词 */
    private String keywords;

    /** 节点状态 */
    private Integer status;
}