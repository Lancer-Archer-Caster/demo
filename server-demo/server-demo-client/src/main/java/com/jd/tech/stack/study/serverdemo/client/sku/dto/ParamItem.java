package com.jd.tech.stack.study.serverdemo.client.sku.dto;

import lombok.Data;

/**
 * Description: 商品参数项
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Data
public class ParamItem {

    /** 参数名 */
    private String name;

    /** 参数值 */
    private String value;
}