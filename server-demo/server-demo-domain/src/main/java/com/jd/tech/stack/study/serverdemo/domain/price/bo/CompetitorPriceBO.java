package com.jd.tech.stack.study.serverdemo.domain.price.bo;

/**
 * Description: 竞品价格BO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class CompetitorPriceBO {

    private String platform;

    private String price;

    private String productName;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}