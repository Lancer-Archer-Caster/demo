package com.jd.tech.stack.study.serverdemo.infra.price.dataobject;

/**
 * Description: 价格对比DO
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class PriceCompareDO {

    private Long id;

    private Long sessionId;

    private Long skuId;

    private String skuName;

    private String ourPrice;

    private String competitorData;

    private String priceAdvantage;

    private String suggestion;

    private String gmtCreate;

    private String gmtModified;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public String getSkuName() { return skuName; }
    public void setSkuName(String skuName) { this.skuName = skuName; }

    public String getOurPrice() { return ourPrice; }
    public void setOurPrice(String ourPrice) { this.ourPrice = ourPrice; }

    public String getCompetitorData() { return competitorData; }
    public void setCompetitorData(String competitorData) { this.competitorData = competitorData; }

    public String getPriceAdvantage() { return priceAdvantage; }
    public void setPriceAdvantage(String priceAdvantage) { this.priceAdvantage = priceAdvantage; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public String getGmtCreate() { return gmtCreate; }
    public void setGmtCreate(String gmtCreate) { this.gmtCreate = gmtCreate; }

    public String getGmtModified() { return gmtModified; }
    public void setGmtModified(String gmtModified) { this.gmtModified = gmtModified; }
}