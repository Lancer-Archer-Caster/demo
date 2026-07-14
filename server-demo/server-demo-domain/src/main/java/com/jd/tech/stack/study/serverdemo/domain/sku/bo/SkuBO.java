package com.jd.tech.stack.study.serverdemo.domain.sku.bo;

import java.util.List;

/**
 * Description: SKU BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SkuBO {

    private String skuId;

    private String skuName;

    private String mainImage;

    private String price;

    private List<ParamItemBO> params;

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

    public List<ParamItemBO> getParams() {
        return params;
    }

    public void setParams(List<ParamItemBO> params) {
        this.params = params;
    }
}