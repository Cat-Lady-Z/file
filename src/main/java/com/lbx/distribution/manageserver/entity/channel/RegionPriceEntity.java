package com.lbx.distribution.manageserver.entity.channel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 城市等级基础价格
 */
public class RegionPriceEntity implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty(value="id")
    private Integer id;

    //渠道表对应id
    @JsonProperty(value="channel_id")
    private Integer channelId;

    //城市等级
    @JsonProperty(value="region_level")
    private String regionLevel;

    //对应基础价格
    @JsonProperty(value="base_price")
    private BigDecimal basePrice;

    //商户id
    @JsonProperty(value="merchant_id")
    private Integer merchantId;

    //公司id
    @JsonProperty(value="company_id")
    private Integer companyId;

    //门店id
    @JsonProperty(value="shop_id")
    private Integer shopId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(String regionLevel) {
        this.regionLevel = regionLevel;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}
