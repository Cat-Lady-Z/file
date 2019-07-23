package com.lbx.distribution.manageserver.entity.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class MerchantConfig {
    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 商户的appkey
     */
    private String appKey;

    /**
     * 商户的secret
     */
    private String secret;

    /**
     * 订单回调地址
     */
    private String orderCallbackUrl;

    /**
     * 消息回调地址
     */
    private String messageCallbackUrl;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public MerchantConfig() {
        super();
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    public String getOrderCallbackUrl() {
        return orderCallbackUrl;
    }

    public void setOrderCallbackUrl(String orderCallbackUrl) {
        this.orderCallbackUrl = orderCallbackUrl == null ? null : orderCallbackUrl.trim();
    }

    public String getMessageCallbackUrl() {
        return messageCallbackUrl;
    }

    public void setMessageCallbackUrl(String messageCallbackUrl) {
        this.messageCallbackUrl = messageCallbackUrl == null ? null : messageCallbackUrl.trim();
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