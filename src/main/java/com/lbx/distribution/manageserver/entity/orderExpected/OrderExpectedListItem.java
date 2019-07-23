package com.lbx.distribution.manageserver.entity.orderExpected;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 预约单列表
 */
public class OrderExpectedListItem {
    /**
     * 订单id
     */
   // @JsonProperty(value="order_id")
    private String orderId;

    /**
     * 商户id
     */
   // @JsonProperty(value="merchant_id")
    private Integer merchantId;

    /**
     * 门店id
     */
    //@JsonProperty(value="shop_id")
    private Integer shopId;

    /**
     * 门店名称
     */
   // @JsonProperty(value="shop_name")
    private String shopName;

    /**
     * 门店的上一级管理公司ID
     */
    //@JsonProperty(value="company_id")
    private Integer companyId;

    /**
     * 第三方订单ID
     */
   // @JsonProperty(value="origin_id")
    private String originId;

    /**
     * 订单类型名称
     */
   // @JsonProperty(value="order_type_name")
    private String orderTypeName;

    /**
     * 订单来源
     */
   // @JsonProperty(value="origin_source")
    private Integer originSource;

    /**
     * 订单来源名称
     */
  // @JsonProperty(value="origin_source_name")
    private String originSourceName;

    /**
     * 订单所在城市的code
     */
   // @JsonProperty(value="city_code")
    private String cityCode;

    /**
     * 订单所在城市的名字
     */
    //@JsonProperty(value="city_name")
    private String cityName;

    /**
     * 预约送达时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   // @JsonProperty(value="expected_delivery_time")
    private Date expectedDeliveryTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   // @JsonProperty(value="create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   // @JsonProperty(value="update_time")
    private Date updateTime;

    /**
     * 预约单发送标识，1:未发送，2：已发送
     */
    //@JsonProperty(value="send_id")
    private Integer sendId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public Integer getOriginSource() {
        return originSource;
    }

    public void setOriginSource(Integer originSource) {
        this.originSource = originSource;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Date expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOriginSourceName() {
        return originSourceName;
    }

    public void setOriginSourceName(String originSourceName) {
        this.originSourceName = originSourceName;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }
}