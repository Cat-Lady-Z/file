package com.lbx.distribution.manageserver.entity.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DistributionConfig {
    /**
     * 配置id
     */
    private Integer confId;

    /**
     * 优先首发时间
     */
    private Integer priorityTime;

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
     * 分发规则(0: 系统自动分发; 1:价格优先; 2: 时间优先; 3:指定渠道)
     */
    private Integer distributeRule;

    /**
     * 分发规则为指定渠道时的渠道id
     */
    private Integer distributeChannelid;

    /**
     * 起始时间(hhmmss)
     */
    private Integer timeStart;

    /**
     * 结束时间(hhmmss)
     */
    private Integer timeEnd;

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

    /**
     * 状态(1.active;0:disable)
     */
    private Integer status;

    public DistributionConfig(Integer confId, Integer merchantId, Integer shopId, Integer companyId, Integer distributeRule, Integer distributeChannelid, Integer timeStart, Integer timeEnd, Date createTime, Date updateTime, Integer status) {
        this.confId = confId;
        this.merchantId = merchantId;
        this.shopId = shopId;
        this.companyId = companyId;
        this.distributeRule = distributeRule;
        this.distributeChannelid = distributeChannelid;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.status = status;
    }

    public DistributionConfig() {
        super();
    }

    public Integer getConfId() {
        return confId;
    }

    public void setConfId(Integer confId) {
        this.confId = confId;
    }

    public Integer getPriorityTime() {
        return priorityTime;
    }

    public void setPriorityTime(Integer priorityTime) {
        this.priorityTime = priorityTime;
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

    public Integer getDistributeRule() {
        return distributeRule;
    }

    public void setDistributeRule(Integer distributeRule) {
        this.distributeRule = distributeRule;
    }

    public Integer getDistributeChannelid() {
        return distributeChannelid;
    }

    public void setDistributeChannelid(Integer distributeChannelid) {
        this.distributeChannelid = distributeChannelid;
    }

    public Integer getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Integer timeStart) {
        this.timeStart = timeStart;
    }

    public Integer getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Integer timeEnd) {
        this.timeEnd = timeEnd;
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