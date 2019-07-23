package com.lbx.distribution.manageserver.entity.order;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class StatOrderEntity implements Serializable {
    /**
     * 渠道ID
     */
    private Integer channelId;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 公司id
     */
    private Integer companyId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 城市编码
     */
    private String cityCode;


    /**
     * 小时订单数
     */
    private Integer orderNum;

    /**
     * 创建时间整点统计(2019-05-27 12:00:00)
     */
    private String createTime;

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

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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