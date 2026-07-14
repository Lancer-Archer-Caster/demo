package com.jd.tech.stack.study.serverdemo.client.script.param;

import lombok.Data;

import java.util.Map;

/**
 * Description: 脚本生成参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ScriptGenerateParam {

    /** 场次ID */
    private Long sessionId;

    /** 商品ID */
    private String skuId;

    /** 模板类型 */
    private String templateType;

    /** 商品参数 */
    private Map<String, String> skuParams;
}