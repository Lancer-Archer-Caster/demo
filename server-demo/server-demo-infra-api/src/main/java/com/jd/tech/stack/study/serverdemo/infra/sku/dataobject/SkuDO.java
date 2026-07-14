package com.jd.tech.stack.study.serverdemo.infra.sku.dataobject;

/**
 * Description: 商品DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class SkuDO {

    private Long id;

    private Long sessionId;

    private String skuId;

    private String skuName;

    private String mainImage;

    private String category;

    private String brand;

    private String price;

    private String stock;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getSkuId() { return skuId; }
    public void setSkuId(String skuId) { this.skuId = skuId; }

    public String getSkuName() { return skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }

    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getStock() { return stock; }
    public void setStock(String stock) { this.stock = stock; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}