package com.jd.tech.stack.study.serverdemo.client.ai.param;

import lombok.Data;

/**
 * Description: 术语翻译参数
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class JargonTranslateParam {

    /** 术语 */
    private String term;

    /** 商品ID */
    private String skuId;

    /** 上下文 */
    private String context;
}