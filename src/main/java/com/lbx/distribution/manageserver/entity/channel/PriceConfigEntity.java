package com.lbx.distribution.manageserver.entity.channel;

import java.util.Date;

/**
 * 价格配置实体
 */
public class PriceConfigEntity {
    /**
     * 配置id
     */
    private Integer confId;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 城市等级
     */
    private Integer cityLevel;

    /**
     * 城市等级基础价格
     */
    private Double levelPrice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态(1.active;0:disable)
     */
    private Integer status;

    public PriceConfigEntity(Integer confId, Integer channelId, Integer merchantId, Integer shopId, Integer companyId, Integer cityLevel, Double levelPrice, Date createTime, Date updateTime, Integer status) {
        this.confId = confId;
        this.channelId = channelId;
        this.merchantId = merchantId;
        this.shopId = shopId;
        this.companyId = companyId;
        this.cityLevel = cityLevel;
        this.levelPrice = levelPrice;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

    public PriceConfigEntity() {
        super();
    }

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(Integer cityLevel) {
        this.cityLevel = cityLevel;
    }

    public Double getLevelPrice() {
        return levelPrice;
    }

    public void setLevelPrice(Double levelPrice) {
        this.levelPrice = levelPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}