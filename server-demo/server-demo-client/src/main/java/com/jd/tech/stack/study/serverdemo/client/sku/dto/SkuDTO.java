package com.jd.tech.stack.study.serverdemo.client.sku.dto;

import java.util.List;

/**
 * Description: 商品DTO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SkuDTO {

    /** 商品ID */
    private String skuId;

    /** 商品名称 */
    private String skuName;

    /** 主图 */
    private String mainImage;

    /** 价格 */
    private String price;

    /** 参数列表 */
    private List<ParamItem> params;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<ParamItem> getParams() {
        return params;
    }

    public void setParams(List<ParamItem> params) {
        this.params = params;
    }
}