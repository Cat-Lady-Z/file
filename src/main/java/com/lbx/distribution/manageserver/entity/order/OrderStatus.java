package com.lbx.distribution.manageserver.entity.order;

import java.util.Date;

public class OrderStatus {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 分发状态(0:未分发;1:分发渠道接单成功;2:已完成;3:已取消)
     */
    private Integer distributeStatus;

    /**
     * 确定分发的配送渠道ID
     */
    private Integer distributeChannelid;

    /**
     * 分发规则(0: 系统自动分发; 1:价格优先; 2: 时间优先; 3:指定渠道)
     */
    private Integer distributeRule;

    /**
     * 
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(Integer distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public Integer getDistributeChannelid() {
        return distributeChannelid;
    }

    public void setDistributeChannelid(Integer distributeChannelid) {
        this.distributeChannelid = distributeChannelid;
    }

    public Integer getDistributeRule() {
        return distributeRule;
    }

    public void setDistributeRule(Integer distributeRule) {
        this.distributeRule = distributeRule;
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
}