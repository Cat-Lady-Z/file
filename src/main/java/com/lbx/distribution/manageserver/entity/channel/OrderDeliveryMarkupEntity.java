package com.lbx.distribution.manageserver.entity.channel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单加价实体
 */
public class OrderDeliveryMarkupEntity implements Serializable {

    private final static long serialVersionUID = 1L;

    @JsonProperty(value="id")
    private Integer id;

    /**
     * 加价距离区间
     */
    @JsonProperty(value="distance_range")
    private String distanceRange;

    /**
     * 加价重量区间
     */
    @JsonProperty(value="weight_range")
    private String weightRange;

    /**
     * 加价时间区间
     */
    @JsonProperty(value="time_range")
    private String timeRange;

    /**
     * 是否包含头，0:否，1:是
     */
    @JsonProperty(value="begin_contain")
    private Integer beginContain;

    /**
     * 是否包含尾，0:否，1:是
     */
    @JsonProperty(value="end_contain")
    private Integer endContain;

    /**
     * 加价单价
     */
    @JsonProperty(value="markup_price")
    private BigDecimal markupPrice;

    /**
     * 加价类型
     */
    @JsonProperty(value="type")
    private Integer type;

    /**
     * 对应渠道（例如：美团，蜂鸟，达达）
     */
    @JsonProperty(value="channel_id")
    private Integer channelId;

    /**
     * 对应商户
     */
    @JsonProperty(value="merchant_id")
    private Integer merchantId;

    /**
     * 公司id
     */
    @JsonProperty(value="company_id")
    private Integer companyId;

    /**
     * 门店id
     */
    @JsonProperty(value="shop_id")
    private Integer shopId;

    /**
     * 更新时间
     */
    @JsonProperty(value="update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonProperty(value="create_time")
    private Date createTime;

    public String getDistanceRange() {
        return distanceRange;
    }

    public void setDistanceRange(String distanceRange) {
        this.distanceRange = distanceRange;
    }

    public String getWeightRange() {
        return weightRange;
    }

    public void setWeightRange(String weightRange) {
        this.weightRange = weightRange;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public BigDecimal getMarkupPrice() {
        return markupPrice;
    }

    public void setMarkupPrice(BigDecimal markupPrice) {
        this.markupPrice = markupPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getBeginContain() {
        return beginContain;
    }

    public void setBeginContain(Integer beginContain) {
        this.beginContain = beginContain;
    }

    public Integer getEndContain() {
        return endContain;
    }

    public void setEndContain(Integer endContain) {
        this.endContain = endContain;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
