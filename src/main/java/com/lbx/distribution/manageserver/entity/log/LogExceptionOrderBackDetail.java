package com.lbx.distribution.manageserver.entity.log;

import java.util.Date;

public class LogExceptionOrderBackDetail {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 渠道ID
     */
    private Integer channelId;

    /**
     * 渠道名字
     */
    private String channelName;

    /**
     * 返回码
     */
    private String exceptionCode;

    /**
     * 返回描述
     */
    private String exceptionMsg;

    /**
     * 全部返回内容
     */
    private String exceptionBody;

    /**
     * 创建时间
     */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode == null ? null : exceptionCode.trim();
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg == null ? null : exceptionMsg.trim();
    }

    public String getExceptionBody() {
        return exceptionBody;
    }

    public void setExceptionBody(String exceptionBody) {
        this.exceptionBody = exceptionBody == null ? null : exceptionBody.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}