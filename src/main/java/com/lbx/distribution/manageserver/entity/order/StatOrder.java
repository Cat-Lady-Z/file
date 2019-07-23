package com.lbx.distribution.manageserver.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

public class StatOrder {
    /**
     * 渠道ID
     */
    private Integer channelId;

    /**
     * 渠道名称
     */
    private Integer channelName;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 小时订单数
     */
    private Integer orderNum;

    /**
     * 创建时间整点统计(2019-05-27 12:00:00)
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelName() {
        return channelName;
    }

    public void setChannelName(Integer channelName) {
        this.channelName = channelName;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}