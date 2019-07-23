package com.lbx.distribution.manageserver.entity.order;

import java.math.BigDecimal;
import java.util.Date;

public class OrderPredictPrice {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 预估配送价格
     */
    private BigDecimal predictPrice;

    /**
     * 基础价
     */
    private BigDecimal basePrice;

    /**
     * 距离加价
     */
    private BigDecimal addDistancePrice;

    /**
     * 重量加价
     */
    private BigDecimal addWeightPrice;

    /**
     * 时间加价
     */
    private BigDecimal addTimePrice;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getPredictPrice() {
        return predictPrice;
    }

    public void setPredictPrice(BigDecimal predictPrice) {
        this.predictPrice = predictPrice;
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getAddDistancePrice() {
        return addDistancePrice;
    }

    public void setAddDistancePrice(BigDecimal addDistancePrice) {
        this.addDistancePrice = addDistancePrice;
    }

    public BigDecimal getAddWeightPrice() {
        return addWeightPrice;
    }

    public void setAddWeightPrice(BigDecimal addWeightPrice) {
        this.addWeightPrice = addWeightPrice;
    }

    public BigDecimal getAddTimePrice() {
        return addTimePrice;
    }

    public void setAddTimePrice(BigDecimal addTimePrice) {
        this.addTimePrice = addTimePrice;
    }
}