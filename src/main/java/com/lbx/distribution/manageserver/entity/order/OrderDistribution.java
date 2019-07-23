package com.lbx.distribution.manageserver.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单配送状态
 */
public class OrderDistribution {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 渠道ID
     */
    private Integer channelId;

    /**
     * 渠道配送活动标识
     */
    private String channelDeliveryId;

    /**
     * 渠道配送 运单id
     */
    private String channelPeisongId;

    /**
     * 实际配送费
     */
    private BigDecimal deliveryFee;

    /**
     * 配送预算金额
     */
    private BigDecimal predictAmount;

    /**
     * 订单状态(已下单=0 待接单＝1 待取货＝2 配送中＝3 已完成＝4 已取消＝5 已过期＝7 指派单=8 妥投异常之物品返回中=9 妥投异常之物品返回完成=10 系统故障订单发布失败=1000 可参考文末的状态说明）
     */
    private Integer statusCode;

    /**
     * 状态描述
     */
    private String statusMsg;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public String getChannelDeliveryId() {
        return channelDeliveryId;
    }

    public void setChannelDeliveryId(String channelDeliveryId) {
        this.channelDeliveryId = channelDeliveryId;
    }

    public String getChannelPeisongId() {
        return channelPeisongId;
    }

    public void setChannelPeisongId(String channelPeisongId) {
        this.channelPeisongId = channelPeisongId;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getPredictAmount() {
        return predictAmount;
    }

    public void setPredictAmount(BigDecimal predictAmount) {
        this.predictAmount = predictAmount;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}